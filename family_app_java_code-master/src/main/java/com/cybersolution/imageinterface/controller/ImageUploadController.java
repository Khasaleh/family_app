package com.cybersolution.imageinterface.controller;

import java.beans.Transient;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.cybersolution.imageinterface.models.ImageModel;
import com.cybersolution.imageinterface.models.User;
import com.cybersolution.imageinterface.service.ImageService;
import com.cybersolution.imageinterface.service.UserService;
import com.cybersolution.imageinterface.service.impl.Dropbox;
import com.google.common.io.Files;

@CrossOrigin(origins = "*", maxAge = 4300)
@RestController
@RequestMapping("/v1/loc")
public class ImageUploadController {
	private static final Logger logger = LogManager.getLogger(ImageUploadController.class);

	@Autowired
	private ImageService imageService;
	@Autowired
	private UserService userService;

	@Autowired
	private RestTemplate restTemplate;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Autowired
	private Dropbox dropbox;

	@Bean
	public WebClient localApiClient() {
		return WebClient.create("http://localhost:8811/duplication-check");
	}

	@Transient
	@PostMapping("/imageupload")
	public ResponseEntity<String> uploadImage(@RequestParam("date") Date date,
			@RequestPart("fileImage") MultipartFile[] multipartFiles, @RequestParam("id") Integer userId)
			throws IOException {
		String fileNameReturned = "";
		if (!multipartFiles[0].isEmpty() && date != null && userId != null) {
			try {
				for (MultipartFile multipartFile : multipartFiles) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

					LocalDateTime now = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
					DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-M-yyyy");
					String formatDateTime = now.format(format);

					ByteArrayResource contentsAsResource = null;
					try {
						contentsAsResource = new ByteArrayResource(multipartFile.getBytes()) {
							@Override
							public String getFilename() {
								return multipartFile.getOriginalFilename();
							}
						};
					} catch (IOException e) {
						logger.error("uploadImage: Failed to read image data: " + e.getMessage());
					}

					MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
					parts.add("imageFile", contentsAsResource);
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.MULTIPART_FORM_DATA);
					HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(parts, headers);
					boolean duplicated = false;
					try {
						JSONObject json = restTemplate.postForEntity("http://localhost:8812/duplication-check",
								requestEntity, JSONObject.class).getBody();
						duplicated = (boolean) json.get("duplicated");
					} catch (Exception e) {
			        	logger.error("uploadImage: Failed to connect to Flask server: "+e.getMessage());
					}

					if (!duplicated) {

						ImageModel image = new ImageModel();
						image.setDate(date);

						String extension = getExtensionByApacheCommonLib(fileName);
						image.setExtension(extension);

						User uploadingUser = userService.getById(userId);
						image.setUser(uploadingUser);

						ImageModel imageSaved = imageService.saveImage(image);
						System.out.println(imageSaved.getId());

						Long imageId = imageSaved.getId();

						if (imageId != null) {
							String newFileName = imageId.toString() + "." + extension;
							fileNameReturned = fileName;
							String dopbox4hourpath = dropbox.upload(multipartFile, newFileName, formatDateTime);
							imageSaved.setLocalPath(dopbox4hourpath);
							imageService.saveImage(image);
						}
					} else {
			        	logger.info("uploadImage: Image Duplicated on Server");
						return new ResponseEntity<String>("Image Duplicated on Server", HttpStatus.NOT_ACCEPTABLE);
					}
				}
			} catch (Exception e) {
	        	logger.error("uploadImage: Failed to read Multipart file: "+e.getMessage());
				return new ResponseEntity<String>("Not Uploaded: " + e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
			}
		} else {
			logger.info("uploadImage: Missing request parameter!");
			return new ResponseEntity<String>("missing data", HttpStatus.NOT_ACCEPTABLE);
		}

		return new ResponseEntity<String>(fileNameReturned, HttpStatus.OK);
	}

	public String getExtensionByApacheCommonLib(String filename) {
		return Files.getFileExtension(filename);
	}

	@DeleteMapping("/images/delete/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {

		boolean image = imageService.delete(id);
		if (image) {
			return new ResponseEntity<>(image, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(image, HttpStatus.NOT_FOUND);
		}
	}

}

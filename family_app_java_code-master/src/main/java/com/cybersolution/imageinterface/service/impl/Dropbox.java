package com.cybersolution.imageinterface.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cybersolution.imageinterface.controller.AuthController;
import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.sharing.RequestedVisibility;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.dropbox.core.v2.sharing.SharedLinkSettings;
import com.dropbox.core.v2.users.DbxUserUsersRequests;
import com.dropbox.core.v2.users.FullAccount;

@Component
public class Dropbox {
	private static final Logger logger = LogManager.getLogger(Dropbox.class);

	final String ROOT_PATH = "/family/";
	private String token;
	public String projectName;

	private DbxRequestConfig config;
	private DbxClientV2 client;
	private FullAccount account;

	@Autowired
	public Dropbox(@Value("${token}") String token, @Value("${dropbox.projectname}") String projectName)
			throws DbxApiException, DbxException {
		this.token = token;
		this.projectName = projectName;
		this.config = new DbxRequestConfig("dropbox/" + projectName);
		this.client = new DbxClientV2(config, token);
		DbxUserUsersRequests r1 = client.users();
		this.account = r1.getCurrentAccount();
	}

	public void list() {
		try {
			ListFolderResult result = client.files().listFolder("");
			while (true) {
				for (Metadata metadata : result.getEntries()) {
				}
				if (!result.getHasMore()) {
					break;
				}
				result = client.files().listFolderContinue(result.getCursor());
			}

		} catch (DbxException ex1) {
			logger.error("dropbox list error: "+ex1.getMessage());
			ex1.printStackTrace();
		}
	}

	public String upload(MultipartFile multipart, String fileName, String date)
			throws FileNotFoundException, IOException, UploadErrorException, DbxException {
		
		Path uploadPath = Paths.get("./");
		Path filePath = uploadPath.resolve(fileName);
		try {
			Files.copy(multipart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
		FileMetadata metadata = null;
		try (InputStream in = new FileInputStream(fileName)) {
			metadata = client.files().uploadBuilder(ROOT_PATH + fileName)
					.uploadAndFinish(in);
			
			
		}
		SharedLinkMetadata slm = client.sharing().createSharedLinkWithSettings(
				ROOT_PATH + fileName,
				SharedLinkSettings.newBuilder().withRequestedVisibility(RequestedVisibility.PUBLIC).build());
		String url = slm.getUrl();
		
		String imageURL = getImageSrc(url);
		Files.delete(filePath);
		

		return imageURL;
	}

	public void delete(String fileName) {
		try {
			client.files().deleteV2(ROOT_PATH + fileName);
		} catch (DbxException e) {
			logger.error("delete: "+e.getMessage());
		}
	}
	

	private String getImageSrc(String url) {
		Document doc;
		String imageURL = "";
		try {
			doc = Jsoup.connect(url).get();
			Elements imgLinks = doc.select("img[src]");
			for (Element innerLink : imgLinks) {
				String innerImgSrc = innerLink.attr("src");
				if (innerImgSrc.contains("p.")) {
					imageURL=innerImgSrc;
				}
			}
		} catch (IOException e) {
			logger.error("getImageSrc: "+e.getMessage());
		}
		return imageURL;
	}
	
}

package com.cybersolution.imageinterface.fileio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

public class FileUploadUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			LOGGER.error("saveFile: Could not save file: " + fileName+", "+e.getMessage());
			throw new IOException("Could not save file: " + fileName, e);
		}
	}

	@Transactional
	public static void deleteImage(String imagePath) throws IOException {
		Path file = Paths.get(imagePath);
		if (!Files.isDirectory(file)) {
			Files.delete(file);
		}
	}

}

package com.cybersolution.imageinterface.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cybersolution.imageinterface.models.ImageModel;
import com.cybersolution.imageinterface.service.ImageService;






@CrossOrigin(origins = "*", maxAge = 4300)
@RestController
@RequestMapping("/v1/api")
public class ImageInterfaceController {
	
	@Autowired
	private ImageService imageService;
	
	
	@GetMapping("/images/{id}")
	public ResponseEntity<List<ImageModel>> listUserImages(@PathVariable("id") Integer id) {
		List<ImageModel> images = imageService.findByUserId(id);
		return new ResponseEntity<>(images, HttpStatus.OK);
	}

	@GetMapping("/images")
	public ResponseEntity<List<ImageModel>> listAllImages() {
		
		List<ImageModel> images = imageService.listAll();
		return new ResponseEntity<>(images, HttpStatus.OK);
	}
}










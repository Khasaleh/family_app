package com.cybersolution.imageinterface.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cybersolution.imageinterface.models.ImageModel;
import com.cybersolution.imageinterface.repository.ImageRepository;
import com.cybersolution.imageinterface.service.ImageService;


@Service

public class ImageServiceImpl implements ImageService {

	@Autowired
	private Dropbox dropbox;
	
	@Autowired
	ImageRepository repo;

	

	@Override
	public List<ImageModel> listAll() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}



	@Override
	public List<ImageModel> findByUserId(Integer id) {
		// TODO Auto-generated method stub
		return repo.findByUserId(id);
	}

	@Override
	public ImageModel saveImage(ImageModel image) {
		
		ImageModel save = repo.save(image);
		
		return save;

	}
	@Transactional
	@Override
	public boolean delete(Long id) {
		Long count = repo.countById(id);

		if (count > 0) {
			ImageModel image = repo.getById(id);
			
			String fileName = image.getId() + "." + image.getExtension();

			LocalDateTime now = LocalDateTime.ofInstant(image.getDate().toInstant(), ZoneId.systemDefault());
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-M-yyyy");
			String date = now.format(format);

			dropbox.delete(fileName);
			repo.deleteById(id);
			return true;
		} else {
			return false;
		}

	}

}

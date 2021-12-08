package com.cybersolution.imageinterface.service;
import java.util.List;

import com.cybersolution.imageinterface.models.ImageModel;

public interface ImageService {
	
	public List<ImageModel> listAll();

	public List<ImageModel> findByUserId(Integer id);

	public ImageModel saveImage(ImageModel image);

	public boolean delete(Long id);
}

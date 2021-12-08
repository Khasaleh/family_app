package com.cybersolution.imageinterface.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cybersolution.imageinterface.models.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
	List<ImageModel> findAll();
	@Query("Select c from ImageModel c where c.user.id=?1")
	List<ImageModel> findByUserId(Integer id);

	@Transactional
	@Modifying
	@Query("Update ImageModel c set c.user.id=null where c.user.id=?1")
	public Integer setUserIdNull(Integer userId); 
	
	Long countById(Long id);
}

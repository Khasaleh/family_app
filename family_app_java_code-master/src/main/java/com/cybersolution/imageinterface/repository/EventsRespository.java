package com.cybersolution.imageinterface.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cybersolution.imageinterface.models.Events;

@Repository 
public interface EventsRespository extends JpaRepository<Events, Integer>{
	
	@Query("Select c from Events c where c.familyId = ?1 and isShared=true")
	public List<Events> findByFamilyIdIfShared(Integer familyId);
	
	@Query("Select c from Events c where c.userId = ?1 ")
	public List<Events> findByUserId(Integer userId);

}

package com.cybersolution.imageinterface.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cybersolution.imageinterface.models.Events;

@Service
public interface EventService {
	
	
	/*
	 * public Events saveEvent(Events event);
	 * 
	 * public boolean deleteEvent(Integer id);
	 */
	
	public boolean updateEvent(Integer id, String event, boolean isShared);
	
	public List<Events> findByFamilyIdIfShared(Integer familyId);
	
	public List<Events> findByUserId(Integer userId);
}

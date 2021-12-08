package com.cybersolution.imageinterface.service.impl;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cybersolution.imageinterface.models.Events;
import com.cybersolution.imageinterface.repository.EventsRespository;
import com.cybersolution.imageinterface.service.EventService;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	private EventsRespository eventRepo;

	@Override
	public boolean updateEvent(Integer id,String event, boolean isShared) {
		Events eventUpd = eventRepo.getById(id);
		if(null != eventUpd) {
			eventUpd.setEvent(event);
			eventUpd.setShared(isShared);
			eventRepo.save(eventUpd);
			return true;
		}
		else
			return false;
		
	}

	@Override
	public List<Events> findByFamilyIdIfShared(Integer familyId) {
		return eventRepo.findByFamilyIdIfShared(familyId);
	
	}

	@Override
	public List<Events> findByUserId(Integer userId) {
		return eventRepo.findByUserId(userId);
	}


}

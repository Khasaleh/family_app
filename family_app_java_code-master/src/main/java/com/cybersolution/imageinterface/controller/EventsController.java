package com.cybersolution.imageinterface.controller;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cybersolution.imageinterface.exception.ResourceNotFoundException;
import com.cybersolution.imageinterface.models.Events;
import com.cybersolution.imageinterface.models.MessageResponse;
import com.cybersolution.imageinterface.repository.EventsRespository;
import com.cybersolution.imageinterface.service.EventService;
import com.cybersolution.imageinterface.service.impl.Messages;


/**
 * @author Faizan
 * Events controller for fetching deleting updating and saving events for users
 *
 */

@CrossOrigin(origins = "*", maxAge = 4300)
@RestController
@RequestMapping("/api/v1/events")
public class EventsController {
	
	private static final Logger LOGGER = LogManager.getLogger(EventsController.class);
	@Autowired
	EventService eventService;
	
	@Autowired
	EventsRespository eventRepo;
	
	@Autowired
	Messages messages;
	
	/**
	 * @apiNote Used to event by id
	 * @Param Integer id
	 * 
	 * @return ResponseEntity (Event)
	 *
	 */
	@GetMapping("/get/eventId/{id}")
	public ResponseEntity getEvent(@PathVariable(value = "id") Integer id) throws ResourceNotFoundException {	
		LOGGER.traceEntry("getEvent -> "+id);
		Events event = eventRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Event not found for id: " + id));
		LOGGER.info("getEvent(): "+event);
		LOGGER.traceExit();
		return new ResponseEntity(event, HttpStatus.OK);
	}
	
	/**
	 * @apiNote Used to delete events
	 * @Param Integer id (eventId)
	 * 
	 * @return Map (deleted,true)
	 *
	 */
	@DeleteMapping("/deleteEvent/{id}")
	public Map<String, Boolean> deleteEvent(@PathVariable(value = "id") Integer eventId)
			throws ResourceNotFoundException {
		LOGGER.traceEntry("deleteEvent -> "+eventId);
		Events event = eventRepo.findById(eventId)
				.orElseThrow(() -> new RuntimeException("Event not found for this id: " + eventId));

		eventRepo.delete(event);
		Map<String, Boolean> response = new HashMap<>();
		LOGGER.info("deleteEvent()");
		response.put("deleted", Boolean.TRUE);
		LOGGER.traceExit();
		return response;
	}
	
	/**
	 * @apiNote Used to get user's events
	 * @Param Integer userId
	 * 
	 * @return ResponseEntity (Event)
	 *
	 */
	@GetMapping("/get/userId/{userId}")
	public ResponseEntity getUserEvents(@PathVariable(value = "userId") Integer userId) {	
		LOGGER.traceEntry("getUserEvents -> "+userId);
		List<Events> event = eventService.findByUserId(userId);
		LOGGER.info("getUserEvents(): "+event);
		LOGGER.traceExit();
		return new ResponseEntity(event, HttpStatus.OK);
	}
	
	/**
	 * @apiNote Used to get user's and Family (shared) events altogether
	 * @Param Integer familyId
	 * @Param Integer userId
	 * 
	 * @return ResponseEntity (Event)
	 *
	 */
	@GetMapping("/get/userId/{userId}/familyId/{familyId}")
	public ResponseEntity getUserWithFamilyEvents(@PathVariable(value = "userId") Integer userId,@PathVariable(value = "familyId") Integer familyId) {	
		LOGGER.traceEntry("getUserWithFamilyEvents -> "+userId+" "+familyId);
		List<Events> famEvent = eventService.findByFamilyIdIfShared(familyId);
		List<Events> event = eventService.findByUserId(userId);
		event.addAll(famEvent);
		LOGGER.info("getUserWithFamilyEvents(): "+event);
		LOGGER.traceExit();
		return new ResponseEntity(event, HttpStatus.OK);
	}
	
	/**
	 * @apiNote Used to get Family (shared) events
	 * @Param Integer familyId
	 * 
	 * @return ResponseEntity (Event)
	 *
	 */
	@GetMapping("/get/familyId/{familyId}")
	public ResponseEntity getFamilyEvents(@PathVariable(value = "familyId") Integer familyId) {	
		LOGGER.traceEntry("getUserWithFamilyEvents -> "+familyId);
		List<Events> events = eventService.findByFamilyIdIfShared(familyId);
		LOGGER.info("getFamilyEvents(): "+events);
		LOGGER.traceExit();
		return new ResponseEntity(events, HttpStatus.OK);
	}
	
	/**
	 * @apiNote Used to update events
	 * @Param Integer id
	 * @Param String event
	 * @Param String date
	 * @Param String startTime
	 * @Param String endTime
	 * @Param boolean isShared
	 * @Param Integer userId
	 * 
	 * @return ResponseEntity (Event)
	 *
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity updateUserEvent(@PathVariable(value = "id") Integer id,
			@RequestParam("event") String event, @RequestParam("date") String date, @RequestParam("startTime") String startTime
			, @RequestParam("endTime") String endTime, @RequestParam("userId") Integer userId, @RequestParam("isShared") boolean isShared)
			throws ResourceNotFoundException {
		
		LOGGER.traceEntry("getUserWithFamilyEvents -> "+id+" "+event+ " "+date+ " "+startTime+ " "+endTime
				+" "+userId+ " "+isShared);
		Events events = eventRepo.findById(id).get();

		if(null != events) {
			if(events.getUserId().equals(userId)) {
				events.setEvent(event);
				events.setShared(isShared);
				eventRepo.save(events);
				LOGGER.info("updateUserEvent(): "+events);
				LOGGER.traceExit();
				return ResponseEntity.ok(events);
			}else {
				LOGGER.error(messages.get("ERROR_EVENT_EDIT"));
				LOGGER.traceExit();
				return ResponseEntity.badRequest().body(new MessageResponse(messages.get("ERROR_EVENT_EDIT")));
			}
			
		}
		else {
			LOGGER.error(messages.get("ERROR_INVALID_EVENT"));
			LOGGER.traceExit();
			return ResponseEntity.badRequest().body(new MessageResponse(messages.get("ERROR_INVALID_EVENT")));
		}
	}
	
	/**
	 * @apiNote Used to save events
	 * @Param IntegeruserId
	 * @Param String event
	 * @Param String date
	 * @Param String startTime
	 * @Param String endTime
	 * @Param boolean isShared
	 * @Param Integer familyId
	 * 
	 * @return ResponseEntity (Event)
	 *
	 */
	@PutMapping("/save/{userId}")
	public ResponseEntity saveUserEvent(@PathVariable(value = "userId") Integer userId,
			@RequestParam("event") String event, @RequestParam("date") String date, @RequestParam("startTime") String startTime
			, @RequestParam("endTime") String endTime, @RequestParam("isShared") Boolean isShared,
			@RequestParam("familyId") Integer familyId)
			throws Exception {
			
		LOGGER.traceEntry("saveUserEvent -> "+userId+" "+event+ " "+date+ " "+startTime+ " "+endTime
				+" "+userId+ " "+isShared+ " "+familyId);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date starttimeVal = null;
			Date endtimeVal = null;
			if(!StringUtils.isEmpty(startTime)) {
				starttimeVal = new Timestamp(formatter.parse(startTime).getTime());
			}
			if(!StringUtils.isEmpty(endTime)) {
				endtimeVal = new Timestamp(formatter.parse(endTime).getTime());
			}
			Events events = new Events(null, date, event, starttimeVal.toString(), endtimeVal.toString(), familyId, userId, isShared);
			eventRepo.save(events);
			LOGGER.info("saveUserEvent(): "+events);
			LOGGER.traceExit();
			return ResponseEntity.ok(events);
	
	}
	
}










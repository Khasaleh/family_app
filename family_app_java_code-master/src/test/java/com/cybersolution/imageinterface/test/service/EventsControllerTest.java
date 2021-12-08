package com.cybersolution.imageinterface.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.cybersolution.imageinterface.controller.EventsController;
import com.cybersolution.imageinterface.models.Events;
import com.cybersolution.imageinterface.repository.EventsRespository;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EventsControllerTest {
	@InjectMocks
	private EventsController eventController;


	@Mock
	EventsRespository eventRepository;

	@Test
	public void saveEventTestCase() throws Exception {
		
		Events event  = new Events(null, "2021-10-26", "Test Event", "2021-10-26 01:00:00.0", "2021-10-26 02:00:00.0", 1, 1, true);
		ResponseEntity<?> result = eventController.saveUserEvent(Integer.valueOf(1), "Test Event","2021-10-26", "2021-10-26 01:00:00", "2021-10-26 02:00:00", Boolean.TRUE, Integer.valueOf(1));
		Object response = result.getBody();
		assertEquals(event, response);
	}
	
	@Test
	public void updateEventTestCase() throws Exception {
		
		Events event  = new Events(Integer.valueOf(1), "2021-10-26", "Test Event", "2021-10-26 00:00:00.0", "2021-10-27 00:00:00.0", 1, 1, true);
		Optional<Events> evnttOp = Optional.of(event);
		
		when(eventRepository.findById(Integer.valueOf(1))).thenReturn(evnttOp);
		ResponseEntity<?> result = eventController.updateUserEvent(Integer.valueOf(1), "TEST Event Update","2021-10-26", "2021-10-26 00:00:00", "2021-10-27 00:00:00", 1 ,Boolean.FALSE);
		Object response = result.getBody();
		event.setShared(false);
		event.setEvent("TEST Event Update");
		assertEquals(event, response);
	}
	
	@Test
	public void deleteEventTestCase() throws Exception {
		
		Events event  = new Events(null, "2021-10-26", "Test Event", "2021-10-26 00:00:00.0", "2021-10-27 00:00:00.0", 1, 1, true);
		Optional<Events> op = Optional.of(event);
		Map<String, Boolean> expected = new HashMap<String, Boolean>();
		expected.put("deleted", Boolean.TRUE);
		
		when(eventRepository.findById(Integer.valueOf(1))).thenReturn(op);
		Map<String, Boolean> result = eventController.deleteEvent(Integer.valueOf(1));
		
		assertEquals(expected, result);
	}


}

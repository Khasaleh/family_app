package com.cybersolution.imageinterface.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.cybersolution.imageinterface.controller.ImageUploadController;
import com.cybersolution.imageinterface.models.ERole;
import com.cybersolution.imageinterface.models.ImageModel;
import com.cybersolution.imageinterface.models.Role;
import com.cybersolution.imageinterface.models.User;
import com.cybersolution.imageinterface.service.ImageService;
import com.cybersolution.imageinterface.service.UserService;
import com.cybersolution.imageinterface.service.impl.Dropbox;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ImageUploadControllerTest {
	@InjectMocks
	private ImageUploadController imageController;

	@Mock
	private ImageService imageService;

	@Mock
	private UserService userService;

	@Mock
	private Dropbox dropbox;

	@Mock
	public RestTemplate restTemplate;

	@Test
	public void deleteImageSucessTestCase() throws Exception {
		Long imageId = 3L;
		boolean expected = true;

		when(imageService.delete(imageId)).thenReturn(true);

		ResponseEntity<Boolean> delete = imageController.delete(imageId);
		assertEquals(expected, delete.getBody());
	}
	@Test
	public void deleteImageFailTestCase() throws Exception {
		Long imageId = 3L;
		boolean expected = false;

		when(imageService.delete(imageId)).thenReturn(false);

		ResponseEntity<Boolean> delete = imageController.delete(imageId);
		assertEquals(expected, delete.getBody());
	}


}

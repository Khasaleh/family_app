package com.cybersolution.imageinterface.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cybersolution.imageinterface.controller.AuthController;
import com.cybersolution.imageinterface.models.ERole;
import com.cybersolution.imageinterface.models.Role;
import com.cybersolution.imageinterface.models.SignupRequest;
import com.cybersolution.imageinterface.models.User;
import com.cybersolution.imageinterface.repository.ImageRepository;
import com.cybersolution.imageinterface.repository.UserRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AuthControllerTest {
	@InjectMocks
	private AuthController authController;


	@Mock
	UserRepository userRepository;

	@Mock
	PasswordEncoder encoder;


	@Mock
	ImageRepository imageRepo;

	@Test
	public void registerUserAccepted() throws Exception {
		String expected = "MessageResponse(message=User registered successfully!)";
		Set<String> roles = new HashSet<String>();
		SignupRequest signupRequest = new SignupRequest("testUser", "test@test.com", roles, "123A2!@kjdsllkd", false);

		when(encoder.encode("123A2!@kjdsllkd")).thenReturn("testEncrypted");
		when(userRepository.findByUsername(signupRequest.getUsername())).thenReturn(null);
		ResponseEntity<?> actual = authController.registerUser(signupRequest);

		Object result = actual.getBody();
		assertEquals(expected, result.toString());
	}

	@Test
	public void registerUserDuplicatedEmail() throws Exception {
		String expected = "MessageResponse(message=Error: Username is already taken!)";
		Set<String> roles = new HashSet<String>();
		SignupRequest signupRequest = new SignupRequest("testUser", "test@test.com", roles, "123A2!@kjdsllkd", false);

		when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(Boolean.TRUE);
		ResponseEntity<?> actual = authController.registerUser(signupRequest);

		Object result = actual.getBody();
		assertEquals(expected, result.toString());
	}

	@Test
	public void deleteUserSucessTestCase() throws Exception {
		Set<Role> userRoles = new HashSet<Role>();
		userRoles.add(new Role(null, ERole.ROLE_USER));
		User user = new User(2, "testUser", "test@test.com", "Encrepted", userRoles, null, false);
		Optional<User> op = Optional.of(user);
		Map<String, Boolean> expected = new HashMap<String, Boolean>();
		expected.put("deleted", Boolean.TRUE);
		Integer userId = 2;

		when(imageRepo.setUserIdNull(userId)).thenReturn(1);
		when(userRepository.findById(userId)).thenReturn(op);

		Map<String, Boolean> result = authController.deleteUser(userId);
		assertEquals(expected, result);
	}

	@Test
	public void enableUserSucessTestCase() throws Exception {
		Set<Role> userRoles = new HashSet<Role>();
		userRoles.add(new Role(null, ERole.ROLE_USER));
		User user = new User(2, "testUser", "test@test.com", "Encrepted", userRoles, null, false);
		Optional<User> op = Optional.of(user);
		boolean expected = true;
		Integer userId = 2;
		boolean enabled = true;
		
			
		when(userRepository.findById(userId)).thenReturn(op);
		when(userRepository.save(user)).thenReturn(user);

		ResponseEntity<User> result = authController.updateUserEnabled(userId, enabled);
		user.setEnabled(enabled);
		assertEquals(expected, result.getBody().isEnabled());
	}

	@Test
	public void updateUserDataSucessTestCase() throws Exception {
		Set<Role> userRoles = new HashSet<Role>();
		userRoles.add(new Role(null, ERole.ROLE_USER));
		User user = new User(2, "testUser", "test@test.com", "Encrepted", userRoles, null, false);
		Optional<User> op = Optional.of(user);
		User expected = user;
		Integer userId = 2;
		String newPassword = "123123123";
		String newEmail = "update@email.com";
		String newUserName = "newUser";
		
		when(encoder.encode(newPassword)).thenReturn("testEncrypted");
		when(userRepository.findById(userId)).thenReturn(op);
		when(userRepository.save(user)).thenReturn(user);

		ResponseEntity<User> result = authController.updateUser(userId, newUserName, newEmail, newPassword);
		user.setEmail(newEmail);
		user.setPassword(newPassword);
		user.setUsername(newUserName);
		assertEquals(expected, result.getBody());
	}
	

}

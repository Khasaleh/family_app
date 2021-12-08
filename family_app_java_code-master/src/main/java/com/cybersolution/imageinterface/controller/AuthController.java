package com.cybersolution.imageinterface.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Signed;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cybersolution.imageinterface.exception.ResourceNotFoundException;
import com.cybersolution.imageinterface.models.ERole;
import com.cybersolution.imageinterface.models.JwtResponse;
import com.cybersolution.imageinterface.models.LoginRequest;
import com.cybersolution.imageinterface.models.MessageResponse;
import com.cybersolution.imageinterface.models.Role;
import com.cybersolution.imageinterface.models.SignupRequest;
import com.cybersolution.imageinterface.models.User;
import com.cybersolution.imageinterface.repository.ImageRepository;
import com.cybersolution.imageinterface.repository.RoleRepository;
import com.cybersolution.imageinterface.repository.UserRepository;
import com.cybersolution.imageinterface.security.jwt.JwtUtils;
import com.cybersolution.imageinterface.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 4300)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger logger = LogManager.getLogger(AuthController.class);

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	ImageRepository imageRepo;

	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<User>> listAllUsers() {
		return ResponseEntity.ok(userRepository.findAll());
	}

	@GetMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> findUserById(@PathVariable(value = "id") Integer userId)
			throws ResourceNotFoundException {

		User user = userRepository.findById(userId).orElseThrow(() -> {
			logger.info("findUserById: User not found for this id: " + userId);
			return new ResourceNotFoundException("User not found for this id :: " + userId);
		});

		return ResponseEntity.ok().body(user);
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> updateUser(@PathVariable(value = "id") Integer userId,
			@RequestParam("username") String username, @RequestParam("email") String email,
			@RequestParam("password") String password) throws ResourceNotFoundException {

		User user = userRepository.findById(userId).orElseThrow(() -> {
			logger.info("updateUser: failed with userId: " + userId);
			return new ResourceNotFoundException("User not found for this id :: " + userId);
		});

		if (username != null) {
			user.setUsername(username);
		}

		if (email != null) {
			user.setEmail(email);
		}

		if (!password.equals("")) {
			user.setPassword(encoder.encode(password));
		}

		final User updatedUser = userRepository.save(user);
		return ResponseEntity.ok(updatedUser);
	}

	@PutMapping("/updateEnbaled/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<User> updateUserEnabled(@PathVariable(value = "id") Integer userId,
			@RequestParam("enabled") boolean enabled) throws ResourceNotFoundException {

		User user = userRepository.findById(userId).orElseThrow(() -> {
			logger.info("updateUserEnabled: failed with userId: " + userId);
			return new ResourceNotFoundException("User not found for this id :: " + userId);
		});

		if (enabled) {
			user.setEnabled(true);
		} else {
			user.setEnabled(false);
		}
		final User updatedUser = userRepository.save(user);
		return ResponseEntity.ok(updatedUser);
	}

	@Transactional
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Integer userId)
			throws ResourceNotFoundException {
		Integer imagesUpdated = imageRepo.setUserIdNull(userId);
		User user = userRepository.findById(userId).orElseThrow(() -> {
			logger.info("deleteUser: failed with userId: " + userId);
			return new RuntimeException("User not found for this id :: " + userId);
		});

		userRepository.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		User user = userRepository.findByUsername(loginRequest.getUsername()).get();

		if (user.isEnabled()) {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());
			return ResponseEntity.ok(new JwtResponse(jwt, null, userDetails.getId(), userDetails.getUsername(),
					userDetails.getEmail(), roles));
		} else {
			logger.info("authenticateUser: User is not approved Yet!, with E-mail: " + user.getEmail());
			return ResponseEntity.badRequest().body(new MessageResponse("User is not approved Yet!"));
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			logger.info("registerUser: Username is already taken: " + signUpRequest.getUsername());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			logger.info("registerUser: Email is already taken: " + signUpRequest.getEmail());
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		User user = new User(null, signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()), null, null, false);

		user.setEnabled(false);
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);

		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

}

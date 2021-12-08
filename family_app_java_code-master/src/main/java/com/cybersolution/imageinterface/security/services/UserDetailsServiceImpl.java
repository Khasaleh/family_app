package com.cybersolution.imageinterface.security.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cybersolution.imageinterface.models.User;
import com.cybersolution.imageinterface.repository.UserRepository;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() ->
				{ 
					logger.error("User Not Found with username: " + username);
					return new UsernameNotFoundException("User Not Found with username: " + username);
				});

		return UserDetailsImpl.build(user);
	}

}

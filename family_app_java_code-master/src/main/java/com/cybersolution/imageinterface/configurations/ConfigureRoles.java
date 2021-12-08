package com.cybersolution.imageinterface.configurations;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cybersolution.imageinterface.models.ERole;
import com.cybersolution.imageinterface.models.Role;
import com.cybersolution.imageinterface.repository.RoleRepository;

@Configuration
public class ConfigureRoles {
//To generate User Roles in DB auto
	@Bean
	CommandLineRunner defaultRoles(RoleRepository roleRepository) {
		return roles -> {
			long count = roleRepository.count();
			if (count < 1) {
				Role admin = new Role(null, ERole.ROLE_ADMIN);
				Role user = new Role(null, ERole.ROLE_USER);
				roleRepository.saveAll(List.of(admin, user));
			}
		};
	}

}

package com.cybersolution.imageinterface;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Appconfig {

//	private static final Logger LOGGER = LogManager.getLogger(ImagesInterfaceMicroserviceApplicationConfig.class);
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}	
	
}

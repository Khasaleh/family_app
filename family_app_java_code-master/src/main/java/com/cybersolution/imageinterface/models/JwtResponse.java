package com.cybersolution.imageinterface.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {
	private String accessToken;
	private String tokenType = "Bearer";
	private Integer id;
	private String username;
	private String email;
	private List<String> roles;
}

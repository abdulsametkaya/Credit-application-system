package com.creditapplication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
	
	@Email(message="Please provide a correct email")
	private String email;
	
	@NotNull(message="Please provide a passwprd")
	private String password;

}

package com.kobe.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
	@NotBlank
	@Email
	String email;

	@NotBlank
	String password;

	@NotBlank
	String name;
}

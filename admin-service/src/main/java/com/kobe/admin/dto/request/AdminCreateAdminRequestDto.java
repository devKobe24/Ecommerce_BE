package com.kobe.admin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class AdminCreateAdminRequestDto {

	@Email
	@NotBlank
	private String email;

	@Size(min = 8)
	@NotBlank
	private String password;

	@NotBlank
	private String name;
}

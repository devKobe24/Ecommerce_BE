package com.kobe.admin.client;

import com.kobe.admin.dto.request.AdminCreateAdminRequestDto;
import com.kobe.admin.dto.request.AdminCreateUserRequestDto;
import com.kobe.common.dto.response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "USER-SERVICE", path = "/api/internal", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

	@GetMapping("/users")
	ResponseEntity<List<UserResponseDto>> getAllUsers();

	@PostMapping("/users")
	ResponseEntity<Void> createUserByAdmin(@RequestBody AdminCreateUserRequestDto request);

	@PostMapping("/admins")
	ResponseEntity<Void> createAdmin(@RequestBody AdminCreateAdminRequestDto request);

	@GetMapping("/users/{userId}")
	ResponseEntity<UserResponseDto> getUserById(@PathVariable("userId") Long userId, @RequestHeader("Authorization") String jwt);

	@DeleteMapping("/users/{userId}")
	ResponseEntity<Void> deleteUserById(@PathVariable("userId") Long userId, @RequestHeader("Authorization") String jwt);
}

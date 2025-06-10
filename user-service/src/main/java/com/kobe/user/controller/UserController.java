package com.kobe.user.controller;

import com.kobe.common.dto.response.UserResponseDto;
import com.kobe.common.security.CustomUserPrincipal;
import com.kobe.user.entity.User;

import com.kobe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/profile")
	public ResponseEntity<UserResponseDto> getMyProfile(@AuthenticationPrincipal CustomUserPrincipal principal) {
		if (principal == null) {
			throw new IllegalArgumentException("인증된 사용자 정보가 없습니다.");
		}

		UserResponseDto response = userService.getMyProfile(principal.getId());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/ping")
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok("pong from user-service");
	}
}

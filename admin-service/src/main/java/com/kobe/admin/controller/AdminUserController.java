package com.kobe.admin.controller;

import com.kobe.admin.dto.request.AdminCreateUserRequestDto;
import com.kobe.admin.service.AdminService;
import com.kobe.common.dto.response.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // ✅ 클래스 단위로 권한 제어
public class AdminUserController {

	private final AdminService adminService;

	// ✅ 유저 전체 조회
	@GetMapping
	public ResponseEntity<List<UserResponseDto>> getAllUsers(@RequestHeader("Authorization") String authHeader) {
		String token = extractToken(authHeader);
		return ResponseEntity.ok(adminService.getAllUsers(token));
	}

	// ✅ 유저 생성
	@PostMapping
	public ResponseEntity<String> createUser(@Valid @RequestBody AdminCreateUserRequestDto request) {
		adminService.createUser(request);
		return ResponseEntity.ok("유저가 성공적으로 생성되었습니다.");
	}

	// ✅ 특정 유저 조회
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId, @RequestHeader("Authorization") String authHeader) {
		String token = extractToken(authHeader);
		UserResponseDto user = adminService.getUserById(userId, token);
		return ResponseEntity.ok(user);
	}

	// ✅ 유저 삭제
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId, @RequestHeader("Authorization") String authHeader) {
		String token = extractToken(authHeader);
		adminService.deleteUser(userId, token);
		return ResponseEntity.noContent().build();
	}

	/**
	 * ✅ Authorization 헤더에서 Bearer 토큰 추출
	 */
	private String extractToken(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
	}
}

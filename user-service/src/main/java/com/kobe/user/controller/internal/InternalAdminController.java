package com.kobe.user.controller.internal;

import com.kobe.admin.dto.request.AdminCreateAdminRequestDto;
import com.kobe.admin.dto.request.AdminCreateUserRequestDto;
import com.kobe.common.dto.response.UserResponseDto;
import com.kobe.user.mapper.UserMapper;
import com.kobe.user.repository.UserRepository;
import com.kobe.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internal") // ✅ 공통 prefix
@RequiredArgsConstructor
public class InternalAdminController {

	private final UserService userService;
	private final UserRepository userRepository;

	// ✅ 전체 유저 조회
	@GetMapping("/users")
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		List<UserResponseDto> responseList = userRepository.findAll().stream()
			.map(UserMapper::toDto)
			.toList();
		return ResponseEntity.ok(responseList);
	}

	// ✅ 관리자 계정 생성
	@PostMapping("/admins")
	public ResponseEntity<Void> createAdmin(@Valid @RequestBody AdminCreateAdminRequestDto request) {
		userService.createAdmin(request);
		return ResponseEntity.ok().build();
	}

	// ✅ 일반 사용자 생성 (관리자에 의하여)
	@PostMapping("/users")
	public ResponseEntity<Long> createUserByAdmin(@Valid @RequestBody AdminCreateUserRequestDto request) {
		Long userId = userService.createUserByAdmin(request); // userService가 userId 반환
		return ResponseEntity.ok(userId);
	}

	// ✅ 특정 사용자 조회
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
		return ResponseEntity.ok(userService.getUserById(userId));
	}

	// ✅ 특정 사용자 삭제
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}
}

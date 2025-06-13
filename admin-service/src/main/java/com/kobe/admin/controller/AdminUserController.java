package com.kobe.admin.controller;

import com.kobe.admin.dto.request.AdminCreateUserRequestDto;
import com.kobe.admin.service.AdminService;
import com.kobe.common.dto.response.CommonApiResponse;
import com.kobe.common.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // ✅ 클래스 단위로 권한 제어
@Tag(name = "Admin User Management", description = "관리자 전용 사용자 관리 API")
public class AdminUserController {

	private final AdminService adminService;

	// ✅ 유저 전체 조회
	@Operation(
		summary = "전체 사용자 조회",
		description = "관리자가 전체 유저 목록을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공적으로 유저 목록을 반환합니다."),
			@ApiResponse(responseCode = "403", description = "관리자 권한 없음")
		}
	)
	@GetMapping
	public ResponseEntity<CommonApiResponse<List<UserResponseDto>>> getAllUsers() {
		List<UserResponseDto> users = adminService.getAllUsers();
		return ResponseEntity.ok(CommonApiResponse.success(users));

	}

	// ✅ 유저 생성
	@Operation(
		summary = "사용자 생성",
		description = "관리자가 새로운 사용자를 생성합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "유저 생성 성공"),
			@ApiResponse(responseCode = "400", description = "요청 바디 오류")
		}
	)
	@PostMapping
	public ResponseEntity<CommonApiResponse<String>> createUser(@Valid @RequestBody @Parameter(description = "생성할 유저 정보", required = true) AdminCreateUserRequestDto request) {
		Long userId = adminService.createUser(request);
		URI location = URI.create("/api/admin/users/" + userId);
		return ResponseEntity.created(location).body(CommonApiResponse.success("유저가 성공적으로 생성되었습니다."));
	}

	// ✅ 특정 유저 조회
	@Operation(
		summary = "특정 사용자 조회",
		description = "ID로 특정 사용자의 정보를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "유저 조회 성공"),
			@ApiResponse(responseCode = "404", description = "해당 유저를 찾을 수 없음")
		}
	)
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDto> getUser(@Parameter(description = "유저 ID", required = true) @PathVariable Long userId) {
		UserResponseDto user = adminService.getUserById(userId);
		return ResponseEntity.ok(user);
	}

	// ✅ 유저 삭제
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
		adminService.deleteUser(userId);
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

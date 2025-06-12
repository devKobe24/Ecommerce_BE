package com.kobe.admin.client;

import com.kobe.admin.dto.request.AdminCreateAdminRequestDto;
import com.kobe.admin.dto.request.AdminCreateUserRequestDto;
import com.kobe.common.dto.response.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class UserServiceClientFallback implements UserServiceClient {

	@Override
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		log.error("❌ [Fallback] getAllUsers 실패");
		return ResponseEntity.status(503).body(Collections.emptyList());
	}

	@Override
	public ResponseEntity<Long> createUserByAdmin(AdminCreateUserRequestDto request) {
		log.error("❌ [Fallback] createUserByAdmin 실패 - 요청: {}", request);
		return ResponseEntity.status(503).build();
	}

	@Override
	public ResponseEntity<Void> createAdmin(AdminCreateAdminRequestDto request) {
		log.error("❌ [Fallback] createAdmin 실패 - 요청: {}", request);
		return ResponseEntity.status(503).build();
	}

	@Override
	public ResponseEntity<UserResponseDto> getUserById(Long userId) {
		log.error("❌ [Fallback] getUserById 실패 - userId: {}", userId);
		return ResponseEntity.status(503).build();
	}

	@Override
	public ResponseEntity<Void> deleteUserById(Long userId) {
		log.error("❌ [Fallback] deleteUserById 실패 - userId: {}", userId);
		return ResponseEntity.status(503).build();
	}
}

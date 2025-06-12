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
	public ResponseEntity<Void> createUserByAdmin(AdminCreateUserRequestDto request) {
		log.warn("⚠️ UserServiceClientFallback#createUser");
		return ResponseEntity.status(503).build();
	}

	@Override
	public ResponseEntity<Void> createAdmin(AdminCreateAdminRequestDto request) {
		log.warn("⚠️ UserServiceClientFallback#createAdmin");
		return ResponseEntity.status(503).build();
	}
}

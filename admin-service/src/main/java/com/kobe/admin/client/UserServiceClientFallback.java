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
	public ResponseEntity<List<UserResponseDto>> getAllUsers(String jwt) {
		// fallback 응답: 비어 있는 목록과 503 응답
		log.warn("⚠️ UserServiceClientFallback#getAllUsers: 유저 서비스 접근 불가");
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

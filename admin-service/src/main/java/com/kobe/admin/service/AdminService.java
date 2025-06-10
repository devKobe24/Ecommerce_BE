package com.kobe.admin.service;

import com.kobe.admin.client.UserServiceClient;
import com.kobe.admin.dto.request.AdminCreateUserRequestDto;
import com.kobe.common.dto.response.UserResponseDto;
import com.kobe.common.exception.CommonErrorCode;
import com.kobe.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

	private final UserServiceClient userServiceClient;

	// ✅ 전체 사용자 목록 조회
	public List<UserResponseDto> getAllUsers(String jwt) {
		ResponseEntity<List<UserResponseDto>> response = userServiceClient.getAllUsers(jwt);
		return handleResponse(response, "유저 목록 조회 실패");
	}

	// ✅ 특정 사용자 상세 조회
	public UserResponseDto getUserById(Long userId, String jwt) {
		ResponseEntity<UserResponseDto> response = userServiceClient.getUserById(userId, jwt);
		return handleResponse(response, "유저 상세 조회 실패");
	}

	// ✅ 일반 사용자 생성
	public void createUser(AdminCreateUserRequestDto request) {
		ResponseEntity<Void> response = userServiceClient.createUserByAdmin(request);
		handleVoidResponse(response, "유저 생성 실패");
	}

	// ✅ 사용자 삭제
	public void deleteUser(Long userId, String jwt) {
		ResponseEntity<Void> response = userServiceClient.deleteUserById(userId, jwt);
		handleVoidResponse(response, "유저 삭제 실패");
	}

	//--------------------------------------------
	// ✅ 공통 응답 핸들링 메서드
	//--------------------------------------------

	private <T> T handleResponse(ResponseEntity<T> response, String errorMessage) {
		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			log.debug("✅ 성공 응답: {}", response.getBody());
			return response.getBody();
		}
		log.warn("⚠️ {}", errorMessage);
		throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
	}

	private void handleVoidResponse(ResponseEntity<Void> response, String errorMessage) {
		if (!response.getStatusCode().is2xxSuccessful()) {
			log.warn("⚠️ {}", errorMessage);
			throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
		log.info("✅ 성공 처리 완료");
	}
}

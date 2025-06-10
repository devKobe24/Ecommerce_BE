package com.kobe.common.exception;

/**
 * 각 서비스가 구현해야 할 공통 ErrorCode 인터페이스
 */
public interface ErrorCode {
	int getStatus(); // 예: 401
	String getMessage(); // 예: "비밀번호가 일치하지 않습니다."
	String getCode(); // 예: "INVALID_PASSWORD"
}

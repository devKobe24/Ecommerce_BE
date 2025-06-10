package com.kobe.common.exception;

import lombok.Getter;

/**
 * 모든 도메인에서 공통으로 던지는 커스텀 예외
 * ErrorCode 인터페이스를 사용하여 서비스별 확장을 허용함
 */
@Getter
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;

	public CustomException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}

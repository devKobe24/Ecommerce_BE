package com.kobe.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * DTO
 * 예외 발생 시 클라이언트에게 반환될 응답 구조
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
	private final int status;
	private final String message;
	private final String code;

	public static ErrorResponse from (ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getStatus(), errorCode.getMessage(), errorCode.getCode());
	}
}

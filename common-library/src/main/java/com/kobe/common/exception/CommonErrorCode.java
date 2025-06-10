package com.kobe.common.exception;

public enum CommonErrorCode implements ErrorCode {
	INTERNAL_SERVER_ERROR(500, "서버가 사용자의 요청을 처리하는 과정에서 내부 오류가 발생했습니다.", "INTERNAL_SERVER_ERROR");

	private final int status;
	private final String message;
	private final String code;

	CommonErrorCode(int status, String message, String code) {
		this.status = status;
		this.message = message;
		this.code = code;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getCode() {
		return code;
	}
}

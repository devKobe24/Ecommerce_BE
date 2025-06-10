package com.kobe.user.exception;

import com.kobe.common.exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {
	INVALID_INPUT_VALUE(400, "입력값이 유효하지 않습니다.", "INVALID_INPUT_VALUE"),
	INVALID_TOKEN(400, "토큰값이 유효하지 않습니다.", "INVALID_TOKEN"),
	UNAUTHORIZED(401,"인증되지 않았습니다.", "UNAUTHORIZED"),
	MISMATCHED_PASSWORD(402, "비밀번호가 일치하지 않습니다.", "MISMATCHED_PASSWORD"),
	MISMATCHED_REFRESH_TOKEN(402, "리프레시 토큰값이 일치하지 않습니다.", "MISMATCHED_PASSWORD"),
	USER_NOT_FOUND(403, "사용자를 찾을 수 없습니다.", "USER_NOT_FOUND"),
	ROLE_NOT_FOUND(403, "역할을 찾을 수 없습니다.", "ROLE_NOT_FOUND"),
	ENTITY_NOT_FOUND(404, "리소스를 찾을 수 없습니다.", "ENTITY_NOT_FOUND"),
	EMAIL_NOT_FOUND(404, "이메일을 찾을 수 없습니다.", "EMAIL_NOT_FOUND"),
	EMAIL_ALREADY_USED(409, "이미 사용중인 이메일입니다.", "EMAIL_ALREADY_USED"),
	DUPLICATE_EMAIL(409, "중복된 이메일입니다.", "DUPLICATE_EMAIL"),
	EMAIL_ALREADY_EXISTS(409, "이미 존재하는 이메일입니다.", "EMAIL_ALREADY_EXISTS"),
	NAME_ALREADY_EXISTS(409, "이미 존재하는 사용자명입니다.", "NAME_ALREADY_EXISTS");

	private final int status;
	private final String message;
	private final String code;

	UserErrorCode(int status, String message, String code) {
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

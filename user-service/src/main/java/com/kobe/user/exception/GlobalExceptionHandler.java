package com.kobe.user.exception;

import com.kobe.common.exception.CustomException;
import com.kobe.common.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// ✅ 커스텀 예외 처리
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
		UserErrorCode code = (UserErrorCode) e.getErrorCode();
		log.warn("❗️CustomException: {}", e.getMessage());
		return ResponseEntity.status(code.getStatus())
			.body(new ErrorResponse(code.getStatus(), code.getMessage(), code.getCode()));
	}

	// ✅ 잘못된 파라미터 예외 처리 (ex:@Valid 실패 등)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException e) {
		log.warn("❗️ Validation Error: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(400, "유효하지 않은 요청입니다.", "VALIDATION_ERROR"));
	}

	// ✅ 지원하지 않는 HTTP 메서드 예외 처리
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
		log.warn("❗️ Method Not Allowed: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
			.body(new ErrorResponse(405, "허용되지 않은 HTTP 메서드입니다.", "METHOD_NOT_ALLOWED"));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpServletResponse response) {
		if (response.isCommitted()) {
			log.warn("❗ 이미 응답된 상태입니다. 추가 응답 방지");
			return null;
		}
		log.warn("❗ 요청 바디 읽기 실패: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(400, "요청 본문이 누락되었거나 잘못되었습니다.", "INVALID_REQUEST_BODY"));
	}

	// ✅ 그 외 일반 예외 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception e, HttpServletResponse response) {
		if (response.isCommitted()) {
			log.warn("❗ 이미 응답된 상태입니다. 추가 응답 방지");
			return null;
		}
		log.error("🔥 Unknown Error", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(500, "서버 내부 오류가 발생했습니다.", "INTERNAL_SERVER_ERROR"));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
		log.warn("❗️ IllegalArgumentException: {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(new ErrorResponse(401, e.getMessage(), "UNAUTHORIZED"));
	}
}

package com.kobe.gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2) // 필터보다 먼저 처리되도록 우선순위 지정
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		// 예외 로그
		ex.printStackTrace();

		// HTTP 상태 지정 (예외 종류에 따라 다양하게 설정 가능)
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String message = "알 수 없는 에러가 발생했습니다.";

		if (ex instanceof ResponseStatusException res) {
			status = resolveHttpStatus(res.getStatusCode());
			message = res.getReason() != null ? res.getReason() : res.getMessage();
		}

		exchange.getResponse().setStatusCode(status);
		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

		Map<String, Object> errorAttributes = new HashMap<>();
		errorAttributes.put("status", status.value());
		errorAttributes.put("error", status.getReasonPhrase());
		errorAttributes.put("message", message);
		errorAttributes.put("path", exchange.getRequest().getPath().toString());

		byte[] errorBytes;
		try {
			errorBytes = objectMapper.writeValueAsBytes(errorAttributes);
		} catch (Exception e) {
			errorBytes = ("{\"status\":500,\"message\":\"JSON 직렬화 실패\"}").getBytes(StandardCharsets.UTF_8);
		}

		DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
		return exchange.getResponse().writeWith(Mono.just(bufferFactory.wrap(errorBytes)));
	}

	/**
	 * HttpStatusCode를 HttpStatus로 안전하게 변환합니다.
	 */
	private HttpStatus resolveHttpStatus(HttpStatusCode statusCode) {
		HttpStatus status = HttpStatus.resolve(statusCode.value());
		return status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR;
	}
}

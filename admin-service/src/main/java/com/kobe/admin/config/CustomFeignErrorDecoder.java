package com.kobe.admin.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class CustomFeignErrorDecoder implements ErrorDecoder {
	@Override
	public Exception decode(String methodKey, Response response) {
		int status = response.status();
		return switch (status) {
			case 400 -> new IllegalArgumentException("잘못된 요청입니다.");
			case 401 -> new SecurityException("인증이 필요합니다.");
			case 403 -> new SecurityException("권한이 없습니다.");
			case 404 -> new IllegalStateException("대상 리소스를 찾을 수 없습니다");
			case 500 -> new IllegalStateException("내부 서버 오류가 발생했습니다.");
			default -> new RuntimeException("Feign 요청 실패: " + status);
		};
	}
}

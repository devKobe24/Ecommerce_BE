package com.kobe.gateway.filter;

import com.kobe.gateway.jwt.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

	private final JwtProvider jwtProvider;

	public AuthFilter(JwtProvider jwtProvider) {
		super(Config.class); // 반드시 있어야 함
		this.jwtProvider = jwtProvider;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			String requestPath = exchange.getRequest().getPath().toString();

			// ✅ 로그인, 회원가입, 토큰 갱신 경로는 인증을 생략
			if (requestPath.startsWith("/api/auth/login") ||
				requestPath.startsWith("/api/auth/register") ||
				requestPath.startsWith("/api/auth/reissue")) {
				return chain.filter(exchange); // 인증 필터 통과
			}

			// ✅ 인증 헤더 확인 및 토큰 검증
			String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				log.warn("Missing or invalid Authorization header: {}", authHeader);
				return unauthorized(exchange, "Missing or invalid Authorization header");
			}

			String token = authHeader.substring(7);
			// ✅ JWT 유효성 검사
			if (!jwtProvider.validateToken(token)) {
				log.warn("Invalid or expired token: {}", token);
				return unauthorized(exchange, "Invalid or expired token");
			}

			// ✅ 정상적인 요청은 다음 필터 체인으로 전달
			return chain.filter(exchange);
		};
	}

	private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
		DataBuffer buffer = exchange.getResponse().bufferFactory()
			.wrap(("{\"code\":401, \"message\":\"" + message + "\"}").getBytes());
		return exchange.getResponse().writeWith(Mono.just(buffer));
	}

	public static class Config {
		// 필요 시 필터 설정 속성 정의
	}
}
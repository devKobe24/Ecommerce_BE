package com.kobe.common.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter // ✅ 추가해주어야 Spring Boot가 값을 주입할 수 있음
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private String secretKey;
	private Duration accessTokenExpiration; // milliseconds
	private Duration refreshTokenExpiration; // milliseconds

	public long getAccessTokenExpirationMillis() {
		return accessTokenExpiration.toMillis();
	}

	public long getRefreshTokenExpirationMillis() {
		return refreshTokenExpiration.toMillis();
	}
}

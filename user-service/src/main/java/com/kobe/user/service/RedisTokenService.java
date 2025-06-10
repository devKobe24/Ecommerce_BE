package com.kobe.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

	private static final String REFRESH_TOKEN_PREFIX = "refresh:";
	private final StringRedisTemplate redisTemplate;

	// RefreshToken 저장 (userId -> token)
	public void saveRefreshToken(Long userId, String refreshToken, long expirationMillis) {
		redisTemplate.opsForValue().set(
			REFRESH_TOKEN_PREFIX + userId,
			refreshToken,
			Duration.ofMillis(expirationMillis)
		);
	}

	// RefreshToken 가져오기
	public String getRefreshToken(Long userId) {
		return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);
	}

	// RefreshToken 삭제
	public void deleteRefreshToken(Long userId) {
		redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
	}
}

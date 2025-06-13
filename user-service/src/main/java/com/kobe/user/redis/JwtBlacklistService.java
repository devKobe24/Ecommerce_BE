package com.kobe.user.redis;

import com.kobe.common.jwt.JwtBlacklistChecker;
import com.kobe.common.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtBlacklistService implements JwtBlacklistChecker {

	private final StringRedisTemplate redisTemplate;
	private final JwtProvider jwtProvider;

	private static final String BLACKLIST_PREFIX = "blacklist:";

	@Override
	public boolean isBlacklisted(String token) {
		String jti = jwtProvider.getJti(token);
		return redisTemplate.hasKey(BLACKLIST_PREFIX + jti);
	}

	public void blacklistToken(String token) {
		String jti = jwtProvider.getJti(token);
		long expiration = jwtProvider.getRemainingTime(token);
		redisTemplate.opsForValue().set(BLACKLIST_PREFIX + jti, "logout", Duration.ofMillis(expiration));
	}
}

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
public class JwtBlacklistService {

	private static final String BLACKLIST = "blacklist:";
	private static final String VALUE = "logout";

	private final StringRedisTemplate redisTemplate;
	private final JwtProvider jwtProvider;

	// ✅ jti 블랙리스트 등록
	public void blacklistToken(String token) {
		String jti = jwtProvider.getJti(token);
		long expiration = jwtProvider.getRemainingTime(token);
		redisTemplate.opsForValue().set(BLACKLIST + jti, VALUE, Duration.ofMillis(expiration));

		if (!redisTemplate.hasKey("blacklist:" + jti)) {
			redisTemplate.opsForValue().set("blacklist:" + jti, "logout", Duration.ofMillis(expiration));
			log.info("🔐 로그아웃 완료 = jti 블랙리스트 등록됨");
		} else {
			log.info("⚠️ 이미 블랙리스트에 등록된 토큰입니다.");
		}
	}

	// ✅ jti 블랙리스트 여부 확인
	public boolean isBlacklisted(String token) {
		String jti = jwtProvider.getJti(token);
		return redisTemplate.hasKey(BLACKLIST + jti);
	}
}

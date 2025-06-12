package com.kobe.common.jwt;

import com.kobe.common.model.Role;
import com.kobe.common.security.CustomUserPrincipal;
import com.kobe.common.utils.TokenIdGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final JwtProperties jwtProperties;
	private Key key;
	private final Map<TokenType, Long> expirationTime = new EnumMap<>(TokenType.class);

	@PostConstruct
	public void initKey() {
		this.key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
	}

	// ✅ 공동 토큰 생성 메서드
	public String generateToken(CustomUserPrincipal principal, TokenType tokenType) {
		long expiration = tokenType == TokenType.ACCESS
			? jwtProperties.getAccessTokenExpiration().toMillis()
			:jwtProperties.getRefreshTokenExpiration().toMillis();

		return Jwts.builder()
			.setSubject(String.valueOf(principal.getId()))
			.claim("email", principal.getEmail())
			.claim("role", principal.getRole().name())
			.claim("type", tokenType.name())
			.setId(TokenIdGenerator.generate())
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	public Claims parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public String getTokenType(String token) {
		return parseClaims(token).get("type", String.class);
	}

	public String getUserId(String token) {
		return parseClaims(token).getSubject();
	}

	public String getJti(String token) {
		return parseClaims(token).getId();
	}

	public long getRemainingTime(String token) {
		Date expiration = parseClaims(token).getExpiration();
		return expiration.getTime() - System.currentTimeMillis();
	}

	// ✅ 사용자용 API
	public String createAccessToken(Long userId, Role role) {
		CustomUserPrincipal principal = new CustomUserPrincipal(userId, null, role);
		return generateToken(principal, TokenType.ACCESS);
	}

	public String createRefreshToken(Long userId) {
		CustomUserPrincipal principal = new CustomUserPrincipal(userId, null, Role.USER); // 기본 Role 지정
		return generateToken(principal, TokenType.REFRESH);
	}

	public Long getUserIdAsLong(String token) {
		return Long.parseLong(getUserId(token));
	}
}

package com.kobe.gateway.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	@Value("${jwt.secret-key}")
	private String secretKey; // ✅ 외부 설정에서 주입

	private Key key;

	@PostConstruct
	public void init() {
		byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}

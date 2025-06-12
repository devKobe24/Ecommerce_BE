package com.kobe.common.utils;

public class TokenUtils {
	public static String extractBearerToken(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		throw new IllegalArgumentException("유효하지 않은 Authorization 헤더입니다.");
	}
}

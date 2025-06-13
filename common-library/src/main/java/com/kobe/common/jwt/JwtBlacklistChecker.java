package com.kobe.common.jwt;

@FunctionalInterface
public interface JwtBlacklistChecker {
	boolean isBlacklisted(String token);
}

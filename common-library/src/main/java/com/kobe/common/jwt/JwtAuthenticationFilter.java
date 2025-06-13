package com.kobe.common.jwt;

import com.kobe.common.model.Role;
import com.kobe.common.security.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final JwtBlacklistChecker blacklistChecker; // ✅ 인터페이스 기반 주입

	public JwtAuthenticationFilter(JwtProvider jwtProvider, JwtBlacklistChecker blacklistChecker) {
		this.jwtProvider = jwtProvider;
		this.blacklistChecker = blacklistChecker;
	}

	public JwtAuthenticationFilter(JwtProvider jwtProvider) {
		this(jwtProvider, token -> false); // 기본: 블랙리스트 체크 안함
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		String token = resolveToken(request);
		if (token != null && jwtProvider.validateToken(token)) {
			Claims claims = jwtProvider.parseClaims(token);
			CustomUserPrincipal principal = new CustomUserPrincipal(
				Long.parseLong(claims.getSubject()),
				claims.get("email", String.class),
				Role.valueOf(claims.get("role", String.class)),
				token // ✅ 토큰 추가
			);

			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}

		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (bearer != null && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		return null;
	}
}

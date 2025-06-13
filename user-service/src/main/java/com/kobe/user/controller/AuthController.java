package com.kobe.user.controller;

import com.kobe.common.dto.response.UserResponseDto;
import com.kobe.common.exception.CustomException;
import com.kobe.common.security.CustomUserPrincipal;
import com.kobe.user.dto.request.LoginRequestDto;
import com.kobe.user.dto.request.RefreshTokenRequestDto;
import com.kobe.user.dto.request.RegisterRequestDto;
import com.kobe.user.dto.response.LoginResponseDto;
import com.kobe.user.dto.response.TokenResponseDto;
import com.kobe.user.exception.UserErrorCode;
import com.kobe.user.redis.JwtBlacklistService;
import com.kobe.user.service.AuthService;
import com.kobe.user.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final UserService userService;
	private final JwtBlacklistService jwtBlacklistService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
		if (request == null) {
			throw new IllegalArgumentException("요청 바디가 비어있습니다.");
		}
		LoginResponseDto response = authService.login(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody RegisterRequestDto request) {
		authService.register(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/access-token")
	public ResponseEntity<TokenResponseDto> refresh(@RequestBody RefreshTokenRequestDto request) {
		String newAccessToken = authService.reissueAccessToken(request.refreshToken());
		return ResponseEntity.ok(new TokenResponseDto(newAccessToken));
	}

	@PostMapping("/reissue-token-pair")
	public ResponseEntity<LoginResponseDto> reissue(@RequestHeader("Authorization") String refreshTokenHeader) {
		String refreshToken = refreshTokenHeader.replace("Bearer ", "");
		LoginResponseDto response = authService.reissue(refreshToken);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/me")
	public ResponseEntity<UserResponseDto> getMyInfo(@AuthenticationPrincipal CustomUserPrincipal principal) {
		if (principal == null) {
			throw new CustomException(UserErrorCode.UNAUTHORIZED);
		}
		UserResponseDto response = userService.getMyInfo(principal.getId());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			try {
				jwtBlacklistService.blacklistToken(token);
			} catch (ExpiredJwtException e) {
				log.warn("⏰ 이미 만료된 토큰으로 로그아웃 요청됨. 블랙리스트 생략.");
			}
		}
		return ResponseEntity.ok("로그아웃 완료");
	}
}

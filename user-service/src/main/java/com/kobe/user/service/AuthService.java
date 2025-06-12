package com.kobe.user.service;

import com.kobe.common.exception.CustomException;
import com.kobe.common.jwt.JwtProperties;
import com.kobe.common.jwt.JwtProvider;
import com.kobe.common.model.Role;
import com.kobe.common.utils.SnowflakeIdGenerator;
import com.kobe.user.dto.request.LoginRequestDto;
import com.kobe.user.dto.request.RegisterRequestDto;
import com.kobe.user.dto.response.LoginResponseDto;
import com.kobe.user.entity.User;
import com.kobe.user.exception.UserErrorCode;
import com.kobe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final RedisTokenService redisTokenService;
	private final JwtProperties jwtProperties;
	private final SnowflakeIdGenerator idGenerator;
	private final UserService userService;

	@Transactional
	public void register(RegisterRequestDto request) {
		Long id = idGenerator.nextId();

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new CustomException(UserErrorCode.EMAIL_ALREADY_USED);
		}

		User user = User.builder()
			.id(id)
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.name(request.getName())
			.role(Role.USER)
			.build();

		userRepository.save(user);
	}

	@Transactional
	public LoginResponseDto login(LoginRequestDto request) {
		User user = userService.getByEmail(request.getEmail());

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new CustomException(UserErrorCode.MISMATCHED_PASSWORD);
		}

		// ✅ 마지막 로그인 시간 업데이트
		user.updateLastLoginAt(LocalDateTime.now());

		// ✅ 토큰 생성
		String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole());
		String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getEmail(), user.getRole());

		// ✅ Redis에 저장
		redisTokenService.saveRefreshToken(user.getId(), refreshToken, jwtProperties.getRefreshTokenExpirationMillis());

		// RefreshToken은 Redis 등에 저장해도 됨
		return new LoginResponseDto(accessToken, refreshToken);
	}

	public String reissueAccessToken(String refreshToken) {
		if (!jwtProvider.validateToken(refreshToken)) {
			throw new IllegalArgumentException("유효하지 않는 Refresh Token입니다.");
		}

		Long userId = jwtProvider.getUserIdAsLong(refreshToken);

		String savedToken = redisTokenService.getRefreshToken(userId);
		if (savedToken == null || !savedToken.equals(refreshToken)) {
			throw new IllegalArgumentException("일치하지 않는 Refresh Token입니다.");
		}

		// Access Token 재발급
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		return jwtProvider.createAccessToken(user.getId(), user.getRole());
	}

	public LoginResponseDto reissue(String refreshToken) {
		// 1. Refresh Token 유효성 검사
		if (!jwtProvider.validateToken(refreshToken)) {
			throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
		}

		// 2. userId 추출
		Long userId = jwtProvider.getUserIdAsLong(refreshToken);

		// 3. Redis에 저장된 토큰과 비교
		String storedToken = redisTokenService.getRefreshToken(userId);
		if (storedToken == null || !storedToken.equals(refreshToken)) {
			throw new IllegalArgumentException("저장된 Refresh Token과 일치하지 않습니다.");
		}

		// 4. 사용자 정보 조회
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

		// 5. 새 Access Token 생성
		String newAccessToken = jwtProvider.createAccessToken(user.getId(), user.getRole());

		// (선택) Refresh Token 재사용 or 재발급
		return new LoginResponseDto(newAccessToken, refreshToken);
	}

	private void validateRefreshToken(String refreshToken) {
		if (!jwtProvider.validateToken(refreshToken)) {
			throw new CustomException(UserErrorCode.INVALID_TOKEN);
		}
		Long userId = jwtProvider.getUserIdAsLong(refreshToken);
		String stored = redisTokenService.getRefreshToken(userId);
		if (!refreshToken.equals(stored)) {
			throw new CustomException(UserErrorCode.MISMATCHED_REFRESH_TOKEN);
		}
	}
}

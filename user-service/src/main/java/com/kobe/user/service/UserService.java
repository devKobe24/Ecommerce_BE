package com.kobe.user.service;

import com.kobe.admin.dto.request.AdminCreateAdminRequestDto;
import com.kobe.admin.dto.request.AdminCreateUserRequestDto;
import com.kobe.common.dto.response.UserResponseDto;
import com.kobe.common.exception.CustomException;
import com.kobe.common.model.Role;
import com.kobe.common.utils.SnowflakeIdGenerator;
import com.kobe.user.entity.User;
import com.kobe.user.exception.UserErrorCode;
import com.kobe.user.mapper.UserMapper;
import com.kobe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final SnowflakeIdGenerator idGenerator;
	private final BCryptPasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public User getByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
	}

	@Transactional
	public Long createUserByAdmin(AdminCreateUserRequestDto request) {
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

	@Transactional(readOnly = true)
	public UserResponseDto getMyInfo(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
		return UserMapper.toDto(user);
	}

	@Transactional(readOnly = true)
	public UserResponseDto getMyProfile(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
		return UserMapper.toDto(user);
	}

	@Transactional
	public void createAdmin(AdminCreateAdminRequestDto request) {
		Long id = idGenerator.nextId();

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new CustomException(UserErrorCode.EMAIL_ALREADY_USED);
		}

		User admin = User.builder()
			.id(id)
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.name(request.getName())
			.role(Role.ADMIN)
			.isActive(true)
			.build();

		userRepository.save(admin);
	}

	@Transactional
	public UserResponseDto getUserById(Long userId) {
		return userRepository.findById(userId)
			.map(UserMapper::toDto)
			.orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
	}

	@Transactional
	public void deleteUser(Long userId) {
		if (!userRepository.existsById(userId)) {
			throw new CustomException(UserErrorCode.USER_NOT_FOUND);
		}
		userRepository.deleteById(userId);
	}
}

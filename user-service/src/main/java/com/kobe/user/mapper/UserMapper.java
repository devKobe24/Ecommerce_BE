package com.kobe.user.mapper;

import com.kobe.common.dto.response.UserResponseDto;
import com.kobe.user.entity.User;

public class UserMapper {

	public static UserResponseDto toDto(User user) {
		// User -> UserResponseDto 변환
		return new UserResponseDto(
			user.getId(),
			user.getEmail(),
			user.getRole(),
			user.getLastLoginAt(),
			user.isActive()
		);
	}
}

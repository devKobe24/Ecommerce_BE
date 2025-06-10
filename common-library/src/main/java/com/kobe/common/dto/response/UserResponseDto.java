package com.kobe.common.dto.response;

import com.kobe.common.model.Role;

import java.time.LocalDateTime;

public record UserResponseDto(Long id, String email, Role role, LocalDateTime lastLoginAt, boolean isActive) {
}

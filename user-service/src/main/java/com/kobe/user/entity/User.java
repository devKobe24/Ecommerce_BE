package com.kobe.user.entity;

import com.kobe.common.model.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

	@Id
	private Long id; // Snowflake에서 직접 설정

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, length = 50)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;

	@Builder.Default
	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;

	public void updateLastLoginAt(LocalDateTime loginTime) {
		this.lastLoginAt = loginTime;
	}

	public void deactivate() {
		this.isActive = false;
	}

	public void activate() {
		this.isActive = true;
	}
}

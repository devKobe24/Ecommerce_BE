package com.kobe.common.security;

import com.kobe.common.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CustomUserPrincipal implements UserDetails {

	private final Long id;
	private final String email;
	private final Role role;
	private final String token; // ✅ JWT 저장용 필드

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	@Override
	public String getPassword() {
		return null; // 비밀번호 인증은 여기서 처리하지 않음
	}

	@Override
	public String getUsername() {
		return email; // 이메일을 사용자 이름으로 사용
	}
}

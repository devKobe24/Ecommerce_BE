package com.kobe.user.config;

import com.kobe.common.jwt.JwtAuthenticationFilter;
import com.kobe.common.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PerAuthorize 활성화.
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtProvider jwtProvider;

	// ✅ Security Filter Chain 설정
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// ✅ JwtAuthenticationFilter를 직접 생성하여 사용
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider);

		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configure(http)) // 필요시 WebMvcConfigurer에서 CORS 세부설정
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Preflight 요청 허용
				.anyRequest().authenticated()
			)
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint(authenticationEntryPoint())
				.accessDeniedHandler(accessDeniedHandler())
			)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// ✅ 비밀번호 암호화용 빈 등록
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// ✅ 인증 실패 (401) 핸들러
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, authException) -> {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.getWriter().write("""
				{
					"code": 401,
					"message": "인증이 필요합니다.",
					"error": "UNAUTHORIZED"
				}
				""");
		};
	}

	// ✅ 인가 실패 (403) 핸들러
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, accessDeniedException) -> {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/json");
			response.getWriter().write("""
				{
					"code": 403,
					"message": "접근 권한이 없습니다.",
					"error": "FORBIDDEN"
				}
			""");
		};
	}
}

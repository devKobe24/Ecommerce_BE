package com.kobe.admin.config;

import com.kobe.common.security.CustomUserPrincipal;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.annotation.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignAuthRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof CustomUserPrincipal userPrincipal) {
				@Nullable String token = userPrincipal.getToken();
				if (token != null && !token.isBlank()) {
					template.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
				}
			}
		}
	}
}

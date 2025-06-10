package com.kobe.admin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger(OpenAPI 3) 구성 설정.
 * JWT 인증을 포함한 보안 스키마를 설정하며, Swagger UI에서 Authorize 버튼을 통해 토큰 인증 테스트 가능.
 */
@Configuration
@OpenAPIDefinition(
	info = @Info(
		title = "User Service API",
		description = "유저 서비스 API 문서입니다.",
		version = "v1",
		contact = @Contact(name = "Kobe", email = "dev.skyachieve91@gmail.com")
	),
	security = @SecurityRequirement(name = "BearerAuth"),
	servers = @Server(url = "http://localhost:8081")
)
@SecurityScheme(
	name = "BearerAuth",
	type = SecuritySchemeType.HTTP,
	scheme = "bearer",
	bearerFormat = "JWT"
)
public class OpenConfig {

	public static final String SECURITY_SCHEME_NAME = "BearerAuth";
	public static final String API_TITLE = "User Service API";
	public static final String API_DESCRIPTION = "Spring Boot 기반 유지 서비스 OpenAPI 문서입니다.";
	public static final String API_VERSION = "v1";

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.components(new Components().addSecuritySchemes(
				SECURITY_SCHEME_NAME,
				new io.swagger.v3.oas.models.security.SecurityScheme()
					.type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")
			))
			.addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList(SECURITY_SCHEME_NAME))
			.info(new io.swagger.v3.oas.models.info.Info()
				.title(API_TITLE)
				.version(API_VERSION)
				.description(API_DESCRIPTION)
				.license(new License().name("Apache 2.0").url("https://www.springdoc.org")));
	}
}

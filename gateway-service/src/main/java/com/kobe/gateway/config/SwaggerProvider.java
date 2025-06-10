package com.kobe.gateway.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerProvider {

	private static final String API_URI = "/v3/api-docs";

	@Bean
	public List<GroupedOpenApi> apis(RouteDefinitionLocator locator) {
		return locator.getRouteDefinitions()
			.filter(route -> route.getId().endsWith("-service")) // 서비스별 routeId 기준
			.map(route -> {
				String name = route.getId();
				String path = "/" + name + API_URI;
				return GroupedOpenApi.builder()
					.group(name)
					.pathsToMatch("/" + name + "/**")
					.addOpenApiCustomizer(openApi -> openApi.info(new Info()
						.title(name + " API")
						.description("Swagger for " + name)
					))
					.build();
			})
			.collectList()
			.block(); // 중요: reactive -> blocking으로 변환
	}
}

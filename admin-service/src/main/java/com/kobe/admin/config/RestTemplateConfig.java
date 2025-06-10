package com.kobe.admin.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	@LoadBalanced // ✅ Eureka에 등록된 서비스 이름 사용 가능하게 함.
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

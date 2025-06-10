package com.kobe.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer // ✅ Eureka 서버 어노테이션 추가 (꼭 필요)
@SpringBootApplication
public class DiscoveryServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServiceApplication.class, args);
	}
}

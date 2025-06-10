package com.kobe.admin;

import com.kobe.common.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {
	"com.kobe.admin",
	"com.kobe.common.jwt",
	"com.kobe.common.security"
})
@EnableConfigurationProperties(JwtProperties.class) // ✅ common-library JwtProperties 등록
@EnableFeignClients(basePackages = "com.kobe.admin.client") // ✅ FeignClient 스캔
public class AdminServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AdminServiceApplication.class, args);
	}
}

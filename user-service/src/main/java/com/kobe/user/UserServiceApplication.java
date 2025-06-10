package com.kobe.user;

import com.kobe.common.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kobe.user", "com.kobe.common"})
@EnableConfigurationProperties(JwtProperties.class) // ✅ @ConfigurationProperties 클래스(JwtProperties)를 Spring 빈으로 명시적으로 등록.
public class UserServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
}

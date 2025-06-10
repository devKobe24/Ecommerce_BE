package com.kobe.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("⚠️ 비밀번호를 인자로 전달해주세요 (예 : ./gradlew :password-generator:run --args='adminPassword1234' -PmainClass=com.kobe.util.PasswordHashGenerator");
			return;
		}

		String rawPassword = args[0];
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(rawPassword);

		System.out.println("🔐 원문 비밀번호: " + rawPassword);
		System.out.println("📌 BCrypt 해시: " + encodedPassword);
	}
}

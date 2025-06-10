package com.kobe.admin.client;

import com.kobe.admin.dto.request.AdminCreateUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


@Component
@RequiredArgsConstructor
public class UserClient {
	private final RestTemplate restTemplate;

	@Value("${user.service.url}")
	private String userServiceUrl;

	private static final String API_USER_CREATE_URL = "/api/internal/users";

	public String getAllUsers(String jwt) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(jwt); // ✅ Authorization: Bearer {jwt}
		HttpEntity<?> request = new HttpEntity<>(headers);

		return restTemplate.exchange(
			"http://USER-SERVICE/api/internal/users", // Eureka serviceId
			HttpMethod.GET,
			request,
			String.class
		).getBody();
	}

	public void createUser(AdminCreateUserRequestDto request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<AdminCreateUserRequestDto> httpEntity = new HttpEntity<>(request, headers);
		restTemplate.postForEntity(userServiceUrl + API_USER_CREATE_URL, httpEntity, Void.class);
	}
}

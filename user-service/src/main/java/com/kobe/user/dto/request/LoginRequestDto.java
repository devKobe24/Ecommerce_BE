package com.kobe.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // Jackson 역직렬화를 위해 기본 생성자 필요
public class LoginRequestDto {
	String email;
	String password;
}

package com.kobe.admin.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class CustomFeignErrorDecoder implements ErrorDecoder {
	@Override
	public Exception decode(String methodKey, Response response) {
		return new RuntimeException("Feign Error: " + response.status());
	}
}

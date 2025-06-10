package com.kobe.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonApiResponse<T> {
	private final boolean success;
	private final T data;

	public static <T> CommonApiResponse<T> success(T data) {
		return new CommonApiResponse<>(true, data);
	}
}

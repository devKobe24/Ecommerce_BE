package com.kobe.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
// ✅ (클라이언트에게 응답용)
public class ProductResponse {
	private Long id;
	private String name;
	private Integer price;
	private String description;
	private String imageUrl;
	private Integer stockQuantity;
	private Boolean active;
	private String category;
	private String createdAt;
	private String updatedAt;
}

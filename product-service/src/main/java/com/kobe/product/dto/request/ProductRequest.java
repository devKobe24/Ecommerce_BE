package com.kobe.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// ✅ (상품 등록/수정 요청 DTO)
public class ProductRequest {

	@NotBlank(message = "상품명은 필수입니다.")
	private String name;

	@NotNull(message = "가격은 필수입니다.")
	private Integer price;

	private String description;

	private String imageUrl;

	@NotNull(message = "재고 수량은 필수입니다.")
	private Integer stockQuantity;

	private Boolean active = true;

	private String category;
}

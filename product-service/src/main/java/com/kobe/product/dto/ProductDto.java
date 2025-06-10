package com.kobe.product.dto;

import com.kobe.product.entity.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

	private Long id;
	private String name;
	private Integer price;
	private String description;
	private String imageUrl;
	private Integer stockQuantity;
	private Boolean active;
	private String category;

	public static ProductDto fromEntity(Product product) {
		return ProductDto.builder()
			.id(product.getId())
			.name(product.getName())
			.price(product.getPrice())
			.description(product.getDescription())
			.imageUrl(product.getImageUrl())
			.stockQuantity(product.getStockQuantity())
			.active(product.getActive())
			.category(product.getCategory())
			.build();
	}
}

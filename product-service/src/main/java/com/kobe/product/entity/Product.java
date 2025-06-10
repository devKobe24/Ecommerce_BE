package com.kobe.product.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class) // ✅ Auditing 활성화
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false)
	private Integer price;

	@Column(length = 1000)
	private String description;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "stock_quantity", nullable = false)
	private Integer stockQuantity;

	@Column(nullable = false)
	private Boolean active;

	@Column(length = 50)
	private String category;

	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;
}

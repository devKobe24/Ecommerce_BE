package com.kobe.product.service;

import com.kobe.product.dto.ProductDto;
import com.kobe.product.entity.Product;
import com.kobe.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// -------------------------------------------------------------
// ❌ 잘못된 import
//import jakarta.transaction.Transactional;
// ✅ 올바른 import
import org.springframework.transaction.annotation.Transactional;
// -------------------------------------------------------------

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	// 🎁 상품 등록
	@Transactional
	public ProductDto createProduct(ProductDto dto) {
		Product product = Product.builder()
			.name(dto.getName())
			.price(dto.getPrice())
			.description(dto.getDescription())
			.imageUrl(dto.getImageUrl())
			.stockQuantity(dto.getStockQuantity())
			.active(dto.getActive())
			.category(dto.getCategory())
			.build();

		Product saved = productRepository.save(product);
		return ProductDto.fromEntity(saved);
	}

	// 🎁 상품 목록 조회
	@Transactional(readOnly = true)
	public List<ProductDto> getAllProducts() {
		return productRepository.findAll()
			.stream()
			.map(ProductDto::fromEntity)
			.toList();
	}

	// 🎁 단일 상품 조회
	@Transactional(readOnly = true)
	public ProductDto getProductById(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
		return ProductDto.fromEntity(product);
	}

	// ❌ 상품 삭제
	@Transactional
	public void deleteProduct(Long id) {
		if (!productRepository.existsById(id)) {
			throw new RuntimeException("해당 상품이 존재하지 않습니다.");
		}
		productRepository.deleteById(id);
	}

	// 🔧 상품 수정
	@Transactional
	public ProductDto updateProduct(Long id, ProductDto dto) {
		Product product = productRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

		product.setName(dto.getName());
		product.setPrice(dto.getPrice());
		product.setDescription(dto.getDescription());
		product.setImageUrl(dto.getImageUrl());
		product.setStockQuantity(dto.getStockQuantity());
		product.setActive(dto.getActive());
		product.setCategory(dto.getCategory());

		return ProductDto.fromEntity(product);
	}
}

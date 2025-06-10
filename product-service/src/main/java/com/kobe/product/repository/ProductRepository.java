package com.kobe.product.repository;

import com.kobe.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
	// 추가로 필요한 쿼리는 여기서 정의
}

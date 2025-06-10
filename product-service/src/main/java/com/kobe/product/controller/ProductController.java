package com.kobe.product.controller;

import com.kobe.product.dto.ProductDto;
import com.kobe.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품 API", description = "상품 등록, 조회, 수정, 삭제 관련 API") // ✅ 추가 팁 (실무 반영): @Tag 를 통해 Controller 전체에 대한 설명 제공.
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@Operation(summary = "상품 등록", description = "신규 상품을 등록합니다.") // ✅ 추가 팁 (실무 반영): 각 메서드별 @Operation 에 summary, description 명확히 분리.
	@ApiResponse(responseCode = "200", description = "상품 등록 성공", content = @Content(schema = @Schema(implementation = ProductDto.class))) // ✅ 추가 팁 (실무 반영): @ApiResponse 로 응답 결과 명세화.
	@PostMapping
	public ResponseEntity<ProductDto> create(
		@Valid @RequestBody(description = "등록할 상품 정보", required = true, // ✅ 추가 팁 (실무 반영): @RequestBody, @Parameter 로 요청 바디와 경로 변수 설명 명시.
			content = @Content(schema = @Schema(implementation = ProductDto.class)))
		ProductDto dto) {
		return ResponseEntity.ok(productService.createProduct(dto));
	}

	@Operation(summary = "상품 전체 조회", description = "모든 상품을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "상품 목록 반환",
		content = @Content(schema = @Schema(implementation = ProductDto.class)))
	@GetMapping
	public ResponseEntity<List<ProductDto>> findAll() {
		return ResponseEntity.ok(productService.getAllProducts());
	}


	@Operation(summary = "상품 단건 조회", description = "상품 ID로 상품을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "상품 조회 성공",
		content = @Content(schema = @Schema(implementation = ProductDto.class)))
	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> findById(
		@Parameter(description = "상품 ID", required = true) // ✅ 추가 팁 (실무 반영): @RequestBody, @Parameter 로 요청 바디와 경로 변수 설명 명시.
		@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@Operation(summary = "상품 수정", description = "기존 상품 정보를 수정합니다.")
	@ApiResponse(responseCode = "200", description = "상품 수정 성공", // ✅ 추가 팁 (실무 반영): 필요 시, HTTP 오류 응답 (400, 404, 500) 등도 명시할 수 있습니다
		content = @Content(schema = @Schema(implementation = ProductDto.class)))
	@PutMapping("/{id}")
	public ResponseEntity<ProductDto> update(
		@Parameter(description = "수정할 상품 ID", required = true)
		@PathVariable Long id,
		@Valid @RequestBody(description = "수정할 상품 정보", required = true,
			content = @Content(schema = @Schema(implementation = ProductDto.class)))
		ProductDto dto) {
		return ResponseEntity.ok(productService.updateProduct(id, dto));
	}

	@Operation(summary = "상품 삭제", description = "상품 ID로 상품을 삭제합니다.")
	@ApiResponse(responseCode = "204", description = "삭제 성공")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
		@Parameter(description = "삭제할 상품 ID", required = true)
		@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/ping")
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok("pong from product-service");
	}
}

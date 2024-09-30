package com.pricewagon.pricewagon.domain.product.api;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pricewagon.pricewagon.domain.product.dto.request.ProductUrlRequest;
import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;
import com.pricewagon.pricewagon.domain.product.dto.response.IndividualProductInfo;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;
import com.pricewagon.pricewagon.domain.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "1. [상품]", description = "상품 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
	private final ProductService productService;

	@Operation(summary = "전체상품 페이지별 조회", description = "쇼핑몰별 모든 상품")
	@GetMapping("/{shopType}")
	public List<Product> getProductsByShopType1(
		@PathVariable ShopType shopType,
		@RequestParam(required = false) Integer lastId,
		@RequestParam(defaultValue = "10") int size
	) {
		return productService.getProductsByShopType(shopType, lastId, size);
	}

	@Operation(summary = "전체상품 페이지별 조회", description = "쇼핑몰별 모든 상품")
	@GetMapping("/{shopType}/1")
	public List<Product> getProductsByShopType2(
		@PathVariable ShopType shopType,
		@PageableDefault(size = 10) Pageable pageable
	) {
		return productService.getProductsByShopType1(shopType, pageable);
	}


	@Operation(summary = "개별 상품 정보 조최", description = "특정 상품에 대한 정보")
	@GetMapping("/{shopType}/{productNumber}")
	public ResponseEntity<IndividualProductInfo> getIndividualProductInfo(
		@PathVariable ShopType shopType,
		@PathVariable Integer productNumber
	) {
		return productService.getIndividualProductInfo(shopType, productNumber);
	}

	@Operation(summary = "상위 카테고리 상품조회", description = "카테고리에 속한 기본 상품정보")
	@GetMapping("/{shopType}/category/{categoryId}")
	public List<BasicProductInfo> getBasicProductsByCategory(
		@PathVariable ShopType shopType,
		@PathVariable Integer categoryId,
		Pageable pageable
	) {
		return productService.getBasicProductsByCategory(shopType, pageable, categoryId);
	}

	@Operation(summary = "상품 등록", description = "크롤링 할 상품 URL 등록")
	@PostMapping("/registration")
	public void registerProductURL(
		@Valid @RequestBody ProductUrlRequest request
	) {
		productService.registerProductURL(request);
	}
}

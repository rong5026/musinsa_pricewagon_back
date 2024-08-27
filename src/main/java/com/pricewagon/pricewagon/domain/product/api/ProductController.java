package com.pricewagon.pricewagon.domain.product.api;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;
import com.pricewagon.pricewagon.domain.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "1. [상품]", description = "상품 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
	private final ProductService productService;

	@Operation(summary = "전체상품 조회", description = "쇼핑몰별 모든 상품 조회")
	@GetMapping("/{shopType}")
	public List<BasicProductInfo> getProductsByShopType(
		@PathVariable ShopType shopType,
		Pageable pageable
	) {
		return productService.getProductsByShopType(shopType, pageable);
	}

}

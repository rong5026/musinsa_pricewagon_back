package com.pricewagon.pricewagon.domain.product.dto;

import java.math.BigDecimal;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;

public record ProductDto(
	Integer id,
	String imgUrl,
	String name,
	Integer productNumber,
	String brand,
	Integer currentPrice,
	BigDecimal starScore,
	Integer reviewCount,
	Integer likeCount,
	ShopType shopType
) {
	public static ProductDto toDTO(Product product) {
		return new ProductDto(
			product.getId(),
			product.getImgUrl(),
			product.getName(),
			product.getProductNumber(),
			product.getBrand(),
			product.getCurrentPrice(),
			product.getStarScore(),
			product.getReviewCount(),
			product.getLikeCount(),
			product.getShopType()
		);
	}
}
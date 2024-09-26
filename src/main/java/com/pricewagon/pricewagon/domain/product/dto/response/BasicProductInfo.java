package com.pricewagon.pricewagon.domain.product.dto.response;

import java.math.BigDecimal;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;

public record BasicProductInfo(
	 Integer productNumber,
	 String name,
	 String brand,
	 BigDecimal starScore,
	 Integer reviewCount,
	 Integer likeCount,
	 String imgUrl,
	 ShopType shopType,
	 Integer currentPrice,
	 Integer previousPrice

) {
	public static BasicProductInfo createHistoryOf (Product product, Integer previousPrice) {
		return new BasicProductInfo(
			product.getProductNumber(),
			product.getName(),
			product.getBrand(),
			product.getStarScore(),
			product.getReviewCount(),
			product.getLikeCount(),
			product.getImgUrl(),
			product.getShopType(),
			product.getCurrentPrice(),
			previousPrice
		);
	}
}

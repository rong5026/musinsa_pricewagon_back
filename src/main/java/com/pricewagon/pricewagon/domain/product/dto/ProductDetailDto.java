package com.pricewagon.pricewagon.domain.product.dto;

import com.pricewagon.pricewagon.domain.product.entity.ProductDetail;

public record ProductDetailDto(
	 Integer highPrice,
	 Integer middlePrice,
	 Integer lowPrice,
	 String productUrl
) {
	public static ProductDetailDto toDTO(ProductDetail productDetail) {
		return new ProductDetailDto(
			productDetail.getHighPrice(),
			productDetail.getMiddlePrice(),
			productDetail.getLowPrice(),
			productDetail.getProductUrl()
		);
	}
}

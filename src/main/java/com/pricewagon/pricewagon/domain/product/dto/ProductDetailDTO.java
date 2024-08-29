package com.pricewagon.pricewagon.domain.product.dto;

import com.pricewagon.pricewagon.domain.product.entity.ProductDetail;

public record ProductDetailDTO(
	 Integer highPrice,
	 Integer middlePrice,
	 Integer lowPrice,
	 String productUrl
) {
	public static ProductDetailDTO toDTO(ProductDetail productDetail) {
		return new ProductDetailDTO(
			productDetail.getHighPrice(),
			productDetail.getMiddlePrice(),
			productDetail.getLowPrice(),
			productDetail.getProductUrl()
		);
	}
}

package com.pricewagon.pricewagon.domain.product.dto.response;

import java.util.List;

import com.pricewagon.pricewagon.domain.product.dto.ProductDetailDTO;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;

public record IndividualProductInfo(

	BasicProductInfo basicProductInfo, // 메인 정보
	ProductDetailDTO productDetail, // 부과 정보
	List<ProductHistory> productHistoryList // 가격 히스토리
	) {

	public static IndividualProductInfo from(Product product, ProductHistory latestHistory) {
		BasicProductInfo basicInfo = BasicProductInfo.createHistoryOf(product, latestHistory);
		ProductDetailDTO productDetail = ProductDetailDTO.toDTO(product.getProductDetail());
		return new IndividualProductInfo(basicInfo, productDetail, product.getProductHistories());
	}
}

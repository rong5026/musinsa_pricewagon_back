package com.pricewagon.pricewagon.domain.product.dto.response;

import java.util.List;

import com.pricewagon.pricewagon.domain.category.dto.response.ParentAndChildCategoryDTO;
import com.pricewagon.pricewagon.domain.product.dto.ProductDetailDto;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;

public record IndividualProductInfo(

	BasicProductInfo basicProductInfo, // 메인 정보
	ProductDetailDto productDetail, // 부과 정보
	List<ProductHistory> productHistoryList, // 가격 히스토리
	ParentAndChildCategoryDTO parentAndChildCategoryDTO // 카테고리
	) {

	public static IndividualProductInfo from(Product product, BasicProductInfo basicProductInfo, ParentAndChildCategoryDTO parentAndChildCategoryDTO) {
		ProductDetailDto productDetail = ProductDetailDto.toDTO(product.getProductDetail());
		return new IndividualProductInfo(basicProductInfo, productDetail, product.getProductHistories(), parentAndChildCategoryDTO);
	}
}

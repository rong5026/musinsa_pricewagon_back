package com.pricewagon.pricewagon.domain.product.dto.response;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;

public record BasicProductInfo(
	 Long id,
	 String name,
	 String brand,
	 Double starScore,
	 Integer reviewCount,
	 Integer likeCount,
	 String imgUrl,
	 Integer originPrice,
	 Integer salePrice,
	 String categoryName,
	 String parentCategoryName

) {
	public static BasicProductInfo of (Product product, ProductHistory productHistory) {
		return new BasicProductInfo(
			product.getId(),
			product.getName(),
			product.getBrand(),
			product.getStarScore(),
			product.getReviewCount(),
			product.getLikeCount(),
			product.getImgUrl(),
			product.getOriginPrice(),
			productHistory.getPrice(),
			product.getCategory() != null ? product.getCategory().getCategoryName() : null ,
			product.getCategory().getParentCategory() != null ? product.getCategory().getParentCategory().getCategoryName() : null
		);
	}
}

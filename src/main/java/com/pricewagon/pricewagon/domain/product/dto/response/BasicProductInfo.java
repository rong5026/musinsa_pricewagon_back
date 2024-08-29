package com.pricewagon.pricewagon.domain.product.dto.response;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;

public record BasicProductInfo(
	 Integer productNumber,
	 String name,
	 String brand,
	 Double starScore,
	 Integer reviewCount,
	 Integer likeCount,
	 String imgUrl,
	 ShopType shopType,
	 Integer originPrice,
	 Integer salePrice
	 // String categoryName,
	 // String parentCategoryName

) {
	public static BasicProductInfo createHistoryOf (Product product, ProductHistory productHistory) {
		return new BasicProductInfo(
			product.getProductNumber(),
			product.getName(),
			product.getBrand(),
			product.getStarScore(),
			product.getReviewCount(),
			product.getLikeCount(),
			product.getImgUrl(),
			product.getShopType(),
			product.getOriginPrice(),
			productHistory != null ? productHistory.getPrice() : 0
			// product.getCategory() != null ? product.getCategory().getCategoryName() : null ,
			// product.getCategory().getParentCategory() != null ? product.getCategory().getParentCategory().getCategoryName() : null
		);
	}
}

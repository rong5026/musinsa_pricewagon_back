package com.pricewagon.pricewagon.domain.product.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShopType {
	MUSINSA("무신사"),
	ZIGZAG("지그재그"),
	ABLY("에이블리"),
	BRANDI("브랜디");
	private final String value;
}

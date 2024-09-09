package com.pricewagon.pricewagon.domain.product.dto.request;

import com.pricewagon.pricewagon.domain.product.entity.ShopType;
import com.pricewagon.pricewagon.global.ValidUrlPattern;

import jakarta.validation.constraints.NotNull;

@ValidUrlPattern
public record ProductUrlRequest(
	@NotNull(message = "URL은 비워둘 수 없습니다.")
	String url,
	@NotNull(message = "쇼핑몰 타입을 선택해주세요.")
	ShopType shopType
) {
}


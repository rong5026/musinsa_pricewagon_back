package com.pricewagon.pricewagon.domain.category.dto;

import com.pricewagon.pricewagon.domain.category.entity.Category;

public record CategoryDTO(
	Integer id,
	String cateroyName
) {
	public static CategoryDTO toDTO(Category category) {
		return new CategoryDTO(
			category.getId(),
			category.getCategoryName()
		);
	}
}

package com.pricewagon.pricewagon.domain.category.dto;

import com.pricewagon.pricewagon.domain.category.entity.Category;

public record CategoryDTO(
	Long id,
	String categoryName
) {
	public static CategoryDTO toDTO(Category category) {
		return new CategoryDTO(
			category.getId(),
			category.getCategoryName()
		);
	}
}

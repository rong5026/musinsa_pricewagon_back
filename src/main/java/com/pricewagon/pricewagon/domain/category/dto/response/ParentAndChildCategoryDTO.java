package com.pricewagon.pricewagon.domain.category.dto.response;

import com.pricewagon.pricewagon.domain.category.dto.CategoryDTO;
import com.pricewagon.pricewagon.domain.category.entity.Category;

public record ParentAndChildCategoryDTO(
	CategoryDTO parentCategory,
	CategoryDTO childCategory
) {
	public static ParentAndChildCategoryDTO from(Category parent, Category child) {
		return new ParentAndChildCategoryDTO(
			CategoryDTO.toDTO(parent),
			CategoryDTO.toDTO(child)
		);
	}
}
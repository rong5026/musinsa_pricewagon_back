package com.pricewagon.pricewagon.domain.category.dto.response;

import java.util.List;

import com.pricewagon.pricewagon.domain.category.dto.CategoryDTO;
import com.pricewagon.pricewagon.domain.category.entity.Category;

public record AllCategoryResponse(
	Integer id,
	String parentCateroyName,
	List<CategoryDTO> categoryList
) {
	public static AllCategoryResponse toDTO(Category parentCategory, List<CategoryDTO> categoryDTOList) {
		return new AllCategoryResponse(
			parentCategory.getId(),
			parentCategory.getCategoryName(),
			categoryDTOList
		);
	}
}

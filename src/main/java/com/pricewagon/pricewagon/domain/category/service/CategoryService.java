package com.pricewagon.pricewagon.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.category.dto.CategoryDTO;
import com.pricewagon.pricewagon.domain.category.dto.response.AllCategoryResponse;
import com.pricewagon.pricewagon.domain.category.entity.Category;
import com.pricewagon.pricewagon.domain.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
	private final CategoryRepository categoryRepository;
	public Category getCategoryById(Long categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new RuntimeException("Category not found"));
	}

	// 부모와 하위 카테고리 모두 포함
	public AllCategoryResponse getParentAndSubCategories(Long parentCategoryId){
		Category parentCategory = getCategoryById(parentCategoryId);
		List<Category> categories = getSubCategories(parentCategoryId);
		List<CategoryDTO> categoryDTOList = categories.stream()
			.map(CategoryDTO::toDTO)
			.toList();
		return AllCategoryResponse.toDTO(parentCategory, categoryDTOList);
	}

	// 하위 카테고리 찾기
	public List<Category> getSubCategories(Long parentCategoryId) {
		return categoryRepository.findAllByParentCategory_Id(parentCategoryId);
	}
}

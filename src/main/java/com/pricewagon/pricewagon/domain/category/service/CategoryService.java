package com.pricewagon.pricewagon.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	// 하위 카테고리 찾기
	public List<Category> getSubcategoriesByParentId(Long parentCategoryId) {
		return categoryRepository.findAllByParentCategory_Id(parentCategoryId);
	}
}

package com.pricewagon.pricewagon.domain.category.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.category.dto.CategoryDTO;
import com.pricewagon.pricewagon.domain.category.dto.response.AllCategoryResponse;
import com.pricewagon.pricewagon.domain.category.dto.response.ParentAndChildCategoryDTO;
import com.pricewagon.pricewagon.domain.category.entity.Category;
import com.pricewagon.pricewagon.domain.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
	private final CategoryRepository categoryRepository;
	public Category getCategoryById(Integer categoryId) {
		return categoryRepository.findById(categoryId)
			.orElseThrow(() -> new RuntimeException("해당 ID의 카테고리가 없습니다"));
	}

	// 부모id로 부모와 하위 카테고리 모두 리턴
	public AllCategoryResponse getParentAndSubCategoriesByParentId(Integer parentCategoryId){
		Category parentCategory = getCategoryById(parentCategoryId);

		List<Category> categories = getSubCategoriesByParentId(parentCategoryId);
		List<CategoryDTO> categoryDTOList = categories.stream()
			.map(CategoryDTO::toDTO)
			.toList();

		return AllCategoryResponse.toDTO(parentCategory, categoryDTOList);
	}

	//부모 ID로 하위 카테고리 찾기
	public List<Category> getSubCategoriesByParentId(Integer parentCategoryId) {
		return categoryRepository.findAllByParentCategory_Id(parentCategoryId);
	}

	// 자식 ID로 부모, 자식 카테고리 리턴
	public ParentAndChildCategoryDTO getParentAndChildCategoriesByChildId(Integer childCategoryId) {
		Category childCategory = getCategoryById(childCategoryId);
		Category parentCategory = childCategory.getParentCategory();

		if (parentCategory == null) {
			throw new RuntimeException("해당 카테고리에는 부모 카테고리가 없습니다.");
		}
		return ParentAndChildCategoryDTO.from(parentCategory, childCategory);
	}
}

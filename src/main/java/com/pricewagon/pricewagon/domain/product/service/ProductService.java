package com.pricewagon.pricewagon.domain.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.category.dto.response.ParentAndChildCategoryDTO;
import com.pricewagon.pricewagon.domain.category.entity.Category;
import com.pricewagon.pricewagon.domain.category.service.CategoryService;
import com.pricewagon.pricewagon.domain.product.dto.request.ProductUrlRequest;
import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;
import com.pricewagon.pricewagon.domain.product.dto.response.IndividualProductInfo;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;
import com.pricewagon.pricewagon.domain.product.repository.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductHistoryService productHistoryService;
	private final CategoryService categoryService;

	// 쇼핑몰에 따른 상품 리스트 조회
	@Transactional(readOnly = true)
	public List<BasicProductInfo> getProductsByShopType(ShopType shopType, Integer lastId, int size) {
		List<Product> products = productRepository.findProductsByShopTypeAndLastId(shopType, lastId, size);

		return products.stream()
			.map(product -> {
				Integer previousPrice = productHistoryService.getDifferentLatestPriceByProductId(product);
				return BasicProductInfo.createHistoryOf(product, previousPrice);
			})
			.toList();
	}


	// 개별 상품 정보 조회
	@Transactional(readOnly = true)
	public ResponseEntity<IndividualProductInfo> getIndividualProductInfo(ShopType shopType, Integer productNumber) {
		Product product = productRepository.findByShopTypeAndProductNumber(shopType, productNumber)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 상품입니다."));

		Category childCategory = product.getCategory();
		ParentAndChildCategoryDTO parentAndChildCategoryDTO = categoryService
			.getParentAndChildCategoriesByChildId(childCategory.getId());

		Integer previousPrice = productHistoryService.getDifferentLatestPriceByProductId(product);
		BasicProductInfo basicProductInfo = BasicProductInfo.createHistoryOf(product, previousPrice);

		IndividualProductInfo individualProductInfo = IndividualProductInfo.from(product, basicProductInfo, parentAndChildCategoryDTO);

		return ResponseEntity.ok(individualProductInfo);
	}

	// 쇼핑몰, 카테고리, 페이지 수로 상품 리스트 조회
	public List<BasicProductInfo> getBasicProductsByCategory(ShopType shopType, Pageable pageable, Integer parentCategoryId) {

		// 상위 카테고리
		Category parentCategory = categoryService.getCategoryById(parentCategoryId);
		// 하위 카테고리
		List<Category> subCategories = categoryService.getSubCategoriesByParentId(parentCategoryId);
		subCategories.add(parentCategory);

		// 카테고리 전체 목록 생성
		List<Integer> categoriesId = new ArrayList<>();
		for (Category category : subCategories) {
			categoriesId.add(category.getId());
		}

		return productRepository.findByShopTypeAndCategory_IdIn(shopType, categoriesId, pageable)
			.stream()
			.map(product -> {
				Integer previousPrice = productHistoryService.getDifferentLatestPriceByProductId(product);
				return BasicProductInfo.createHistoryOf(product, previousPrice);
			})
			.toList();
	}

	public void registerProductURL(ProductUrlRequest request) {


	}
}

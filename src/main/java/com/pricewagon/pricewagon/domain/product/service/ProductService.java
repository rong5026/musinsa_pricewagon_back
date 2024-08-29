package com.pricewagon.pricewagon.domain.product.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.category.entity.Category;
import com.pricewagon.pricewagon.domain.category.service.CategoryService;
import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;
import com.pricewagon.pricewagon.domain.product.dto.response.IndividualProductInfo;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;
import com.pricewagon.pricewagon.domain.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductHistoryService productHistoryService;
	private final CategoryService categoryService;

	@Transactional(readOnly = true)
	public List<BasicProductInfo> getProductsByShopType(ShopType shopType, Pageable pageable) {
		List<Product> products = productRepository.findAllByShopType(shopType, pageable).getContent();

		return products.stream()
			.map(product -> {
				ProductHistory latestHistory = productHistoryService.getLatestHistory(product);
				return BasicProductInfo.createHistoryOf(product, latestHistory);
			})
			.toList();
	}

	@Transactional(readOnly = true)
	public ResponseEntity<IndividualProductInfo> getIndividualProductInfo(ShopType shopType, Integer productNumber) {
		Product product = productRepository.findByShopTypeAndProductNumber(shopType, productNumber)
			.orElseThrow(() -> new RuntimeException("Product not found"));

		ProductHistory latestHistory = getLatestProductHistory(product.getProductHistories())
			.orElseThrow(() -> new RuntimeException("No ProductHistory found"));

		IndividualProductInfo individualProductInfo = IndividualProductInfo.from(product, latestHistory);

		return ResponseEntity.ok(individualProductInfo);
	}

	public List<BasicProductInfo> getBasicProductsByCategory(ShopType shopType, Pageable pageable, Long categoryId) {

		// 상위 카테고리
		Category parentCategory = categoryService.getCategoryById(categoryId);
		// 하위 카테고리
		List<Category> subCategories = categoryService.getSubCategories(categoryId);
		subCategories.add(parentCategory);

		// 카테고리 전체 목록 생성
		List<Long> categoriesId = new ArrayList<>();
		for (Category category : subCategories) {
			categoriesId.add(category.getId());
		}

		return productRepository.findByShopTypeAndCategory_IdIn(shopType, categoriesId, pageable)
			.stream()
			.map(product -> {
				ProductHistory latestHistory = productHistoryService.getLatestHistory(product);
				return BasicProductInfo.createHistoryOf(product, latestHistory);
			})
			.toList();
	}

	private Optional<ProductHistory> getLatestProductHistory(List<ProductHistory> productHistories) {
		return productHistories.stream()
			.max(Comparator.comparing(ProductHistory::getCreatedAt));
	}
}

package com.pricewagon.pricewagon.domain.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;
import com.pricewagon.pricewagon.domain.product.repository.ProductHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductHistoryService {

	private final ProductHistoryRepository productHistoryRepository;
	// 최근 히스토리 반환
	public ProductHistory getLatestHistoryByProductId(Long productId) {
		return productHistoryRepository.findTopByProductIdOrderByCreatedAtDesc(productId)
			.orElseThrow(() -> new RuntimeException("상품 History가 존재하지 않습니다."));
	}

	public Integer getDistinctOrLatestPriceByProductId(Long productId) {
		ProductHistory latestHistory = getLatestHistoryByProductId(productId);

		Page<ProductHistory> result = productHistoryRepository.findFirstByProductIdAndPriceNot(productId,  latestHistory.getPrice(), PageRequest.of(0, 1));
		ProductHistory previousHistory = result.getContent().isEmpty() ? latestHistory : result.getContent().get(0);

		// 최근 값과 다른 첫 번째 값을 찾고, 없으면 최근 값을 리턴
		return previousHistory.getPrice();
	}
}

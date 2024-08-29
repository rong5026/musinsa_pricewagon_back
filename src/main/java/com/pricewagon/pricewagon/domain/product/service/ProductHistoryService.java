package com.pricewagon.pricewagon.domain.product.service;

import java.util.Comparator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductHistoryService {

	// 최근 히스토리 반환
	public ProductHistory getLatestHistory(Product product) {
		return product.getProductHistories().stream()
			.max(Comparator.comparing(ProductHistory::getCreatedAt)) // 가장 최근의 히스토리 가져오기
			.orElse (null);
	}
}

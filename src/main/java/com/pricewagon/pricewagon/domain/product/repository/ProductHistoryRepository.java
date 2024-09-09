package com.pricewagon.pricewagon.domain.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {

	// 특정 상품의 최근값과 다른 ProductHistory 1개 리턴
	Optional<ProductHistory> findFirstByProductIdAndPriceNotOrderByCreatedAtDesc(Long productId, Integer currentPrice);
}


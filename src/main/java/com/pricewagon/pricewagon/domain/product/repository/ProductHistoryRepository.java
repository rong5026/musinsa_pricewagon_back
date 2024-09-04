package com.pricewagon.pricewagon.domain.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {
	Optional<ProductHistory> findTopByProductIdOrderByCreatedAtDesc(Long productId);

	// 특정 상품의 최근값과 다른 ProductHistory 찾기
	@Query("SELECT ph FROM ProductHistory ph WHERE ph.product.id = :productId AND ph.price <> :latestPrice ORDER BY ph.createdAt DESC")
	Optional<ProductHistory> findFirstByProductIdAndPriceNot(Long productId, Integer latestPrice);
}

package com.pricewagon.pricewagon.domain.product.repository;

import java.util.Optional;

import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;

public interface ProductHistoryCustomRepository {
	Optional<ProductHistory> findFirstByProductIdAndPriceNot(Integer productId, Integer currentPrice);
}

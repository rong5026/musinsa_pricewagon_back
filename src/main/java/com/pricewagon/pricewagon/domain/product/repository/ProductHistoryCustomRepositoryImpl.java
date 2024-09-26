package com.pricewagon.pricewagon.domain.product.repository;

import static com.pricewagon.pricewagon.domain.product.entity.QProductHistory.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductHistoryCustomRepositoryImpl implements ProductHistoryCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;
	@Override
	public Optional<ProductHistory> findFirstByProductIdAndPriceNot(Integer productId, Integer currentPrice) {
		ProductHistory result = jpaQueryFactory.selectFrom(productHistory)
			.where(productHistory.product.id.eq(productId)
				.and(productHistory.price.ne(currentPrice)))
			.orderBy(productHistory.createdAt.desc())
			.fetchFirst();

		return Optional.ofNullable(result);
	}
}

package com.pricewagon.pricewagon.domain.product.repository;

import static com.pricewagon.pricewagon.domain.product.entity.QProduct.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;
import com.pricewagon.pricewagon.domain.product.repository.product.ProductRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Product> findProductsByShopTypeAndLastId(ShopType shopType, Integer lastId, int size) {
		// QueryDSL을 사용하여 동적 쿼리 작성 및 실행
		return jpaQueryFactory
			.selectFrom(product)
			.where(gtProductId(lastId))
			.orderBy(product.id.asc()) // id 기준 오름차순 정렬
			.limit(size) // 페이지 크기 설정
			.fetch();
	}

	private BooleanExpression gtProductId(Integer lastProductId) {
		return lastProductId == null ? null : product.id.gt(lastProductId);
	}
}

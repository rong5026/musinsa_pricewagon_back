package com.pricewagon.pricewagon.domain.product.repository;

import static com.pricewagon.pricewagon.domain.product.entity.QProduct.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;
import com.pricewagon.pricewagon.domain.product.repository.product.ProductRepositoryCustom;
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
			.where(
				shopType != null ? product.shopType.eq(shopType) : null, // shopType 조건 추가
				lastId != null ? product.id.gt(lastId) : null // lastId 이후 데이터만 조회
			)
			.orderBy(product.id.asc()) // id 기준 오름차순 정렬
			.limit(size) // 페이지 크기 설정
			.fetch();
	}
}

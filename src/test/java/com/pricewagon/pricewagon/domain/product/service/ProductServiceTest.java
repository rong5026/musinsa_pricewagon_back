package com.pricewagon.pricewagon.domain.product.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;
import com.pricewagon.pricewagon.domain.product.repository.product.ProductRepository;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductService productService;

	/**
	 * Offset 방식과 No-Offset 방식의 성능 비교 테스트
	 */
	@Test
	public void comparePagingPerformance() {
		ShopType shopType = ShopType.MUSINSA;
		int pageSize = 10;

		// Offset 방식 성능 테스트
		long offsetStartTime = System.currentTimeMillis();
		Pageable pageable = PageRequest.of(0, pageSize); // 첫 페이지부터 시작
		List<Product> offsetProducts = productRepository.findAllByShopType(shopType, pageable).getContent();
		long offsetEndTime = System.currentTimeMillis();

		// No-Offset 방식 성능 테스트 (처음에는 lastId가 null로 시작)
		long noOffsetStartTime = System.currentTimeMillis();
		List<Product> noOffsetProducts =  productRepository.findProductsByShopTypeAndLastId(shopType, 9992, pageSize);
		long noOffsetEndTime = System.currentTimeMillis();

		System.out.println("Offset 방식 조회 시간 (ms): " + (offsetEndTime - offsetStartTime));
		System.out.println("No-Offset 방식 조회 시간 (ms): " + (noOffsetEndTime - noOffsetStartTime));
		// 결과 출력
		System.out.println("Offset 방식 조회된 상품 수: " + offsetProducts.size());
		System.out.println("No-Offset 방식 조회된 상품 수: " + noOffsetProducts.size());

	}
}
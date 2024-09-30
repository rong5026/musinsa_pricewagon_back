package com.pricewagon.pricewagon.domain.product.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pricewagon.pricewagon.domain.product.dto.ProductDto;
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
	void comparePagingPerformance() {
		ShopType shopType = ShopType.MUSINSA;
		int pageSize = 10;

		// Offset 방식 첫 페이지 성능 테스트
		long offsetFirstPageStartTime = System.currentTimeMillis();
		Pageable firstPageable = PageRequest.of(0, pageSize);
		List<Product> offsetFirstPageProducts = productRepository.findAllByShopType(shopType, firstPageable).getContent();
		long offsetFirstPageEndTime = System.currentTimeMillis();

		// Offset 방식 마지막 페이지 성능 테스트
		long offsetLastPageStartTime = System.currentTimeMillis();
		Pageable lastPageable = PageRequest.of(14990, pageSize);
		List<Product> offsetLaststPageProducts = productRepository.findAllByShopType(shopType, lastPageable).getContent();
		long offsetLastPageEndTime = System.currentTimeMillis();

		// No-Offset 방식 성능 테스트 (처음에는 lastId가 null로 시작)
		long noOffsetStartTime = System.currentTimeMillis();
		List<Product> noOffsetProducts =  productRepository.findProductsByShopTypeAndLastId(shopType, 149990, pageSize);
		long noOffsetEndTime = System.currentTimeMillis();


		System.out.println("Offset 첫페이지 방식 조회 시간 (ms): " + (offsetFirstPageEndTime - offsetFirstPageStartTime));
		System.out.println("Offset 마지막 페이지 방식 조회 시간 (ms): " + (offsetLastPageEndTime - offsetLastPageStartTime));
		System.out.println("No-Offset 방식 조회 시간 (ms): " + (noOffsetEndTime - noOffsetStartTime));

		System.out.println("Offset 방식 첫페이지 조회된 상품 수: " + offsetFirstPageProducts.size());
		System.out.println("Offset 방식 마지막 페이지 조회된 상품 수: " + offsetLaststPageProducts.size());
		System.out.println("No-Offset 방식 마지막 페이지 조회된 상품 수: " + noOffsetProducts.size());

		assertEquals(offsetFirstPageProducts.size(), noOffsetProducts.size(), "두 방식의 조회된 상품 수가 다릅니다.");

		for (int i = 0; i < offsetLaststPageProducts.size(); i++) {
			Product offsetProduct = offsetLaststPageProducts.get(i);
			Product noOffsetProduct = noOffsetProducts.get(i);

			ProductDto offsetProductDto = ProductDto.toDTO(offsetProduct);
			ProductDto noOffsetProductDto = ProductDto.toDTO(noOffsetProduct);

			assertEquals(offsetProductDto, noOffsetProductDto, "Offset 방식과 No-Offset 방식의 " + (i + 1) + "번째 상품이 다릅니다.");
		}
	}
}
package com.pricewagon.pricewagon.domain.product.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pricewagon.pricewagon.domain.product.dto.response.BasicProductInfo;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;
import com.pricewagon.pricewagon.domain.product.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	public List<BasicProductInfo> getProductsByShopType(ShopType shopType, Pageable pageable) {
		List<Product> products = productRepository.findAllByShopType(shopType, pageable).getContent();

		return products.stream()
			.map(product -> {
				ProductHistory latestHistory = product.getProductHistories().stream()
					.sorted((h1, h2) -> h2.getCreatedAt().compareTo(h1.getCreatedAt())) // 최신 순으로 정렬
					.findFirst() // 첫 번째 요소를 가져옵니다.
					.orElse(null);

				return BasicProductInfo.of(product, latestHistory);
			})
			.toList();
	}
}

package com.pricewagon.pricewagon.domain.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pricewagon.pricewagon.domain.category.entity.Category;
import com.pricewagon.pricewagon.domain.category.repository.CategoryRepository;
import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ProductDetail;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;
import com.pricewagon.pricewagon.domain.product.repository.product.ProductRepository;
import com.pricewagon.pricewagon.domain.product.repository.productdetail.ProductDetailRepository;

import lombok.RequiredArgsConstructor;

@Component
@Profile("test")
@RequiredArgsConstructor
public class ProductDataLoader implements CommandLineRunner {

	private final ProductRepository productRepository;
	private final ProductDetailRepository productDetailRepository;
	private final CategoryRepository categoryRepository;

	@Override
	@Transactional
	public void run(String... args) {
		// 더미 데이터 10,000개 생성
		List<Product> products = new ArrayList<>();
		String[] brands = {"Nike", "Adidas", "Puma", "Uniqlo", "Gucci"};
		String[] productNames = {"T-shirt", "Shoes", "Hat", "Jacket", "Bag"};
		ShopType[] shopTypes = ShopType.values();

		Random random = new Random();
		Category category = categoryRepository.findById(1).orElse(null);

		for (int i = 10000; i < 150000; i++) {

			ProductDetail productDetail = ProductDetail.builder()
				.highPrice(1000)
				.middlePrice(400)
				.lowPrice(100)
				.productUrl("testurl" + i)
				.build();

			productDetail = productDetailRepository.save(productDetail);

			Product product = Product.builder()
				.name(brands[random.nextInt(brands.length)] + " " + productNames[random.nextInt(productNames.length)])
				.brand(brands[random.nextInt(brands.length)])
				.productNumber(100000 + i)
				.currentPrice(10000 + random.nextInt(90000))
				.starScore(BigDecimal.valueOf(3.0 + (5.0 - 3.0) * random.nextDouble()))
				.reviewCount(10 + random.nextInt(500))
				.likeCount(50 + random.nextInt(500))
				.shopType(shopTypes[0])
				.imgUrl("https://dummyimage.com/200x200/000/fff&text=Product" + (i + 1))
				.category(category)
				.productDetail(productDetail)
				.build();

			products.add(product);
		}

		productRepository.saveAll(products);
	}
}
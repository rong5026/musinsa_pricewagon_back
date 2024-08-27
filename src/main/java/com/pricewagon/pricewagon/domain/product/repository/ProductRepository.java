package com.pricewagon.pricewagon.domain.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findAllByShopType(ShopType shopType, Pageable pageable);
}

package com.pricewagon.pricewagon.domain.product.repository.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;

public interface ProductRepository extends JpaRepository<Product, Integer> , ProductRepositoryCustom{
	Page<Product> findAllByShopType(ShopType shopType, Pageable pageable);

	Optional<Product> findByShopTypeAndProductNumber(ShopType shopType, Integer productNumber);
	Page<Product> findByShopTypeAndCategory_IdIn(ShopType shopType, List<Integer> categoryIds, Pageable pageable);

}

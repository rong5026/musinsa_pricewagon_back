package com.pricewagon.pricewagon.domain.product.repository.productdetail;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.product.entity.ProductDetail;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
}

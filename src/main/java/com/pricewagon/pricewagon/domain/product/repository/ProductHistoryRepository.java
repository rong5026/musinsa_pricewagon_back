package com.pricewagon.pricewagon.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> , ProductHistoryCustomRepository{


}


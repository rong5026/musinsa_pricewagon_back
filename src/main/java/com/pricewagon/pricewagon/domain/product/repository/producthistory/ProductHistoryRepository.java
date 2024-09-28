package com.pricewagon.pricewagon.domain.product.repository.producthistory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.product.entity.ProductHistory;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Integer> , ProductHistoryCustomRepository{


}


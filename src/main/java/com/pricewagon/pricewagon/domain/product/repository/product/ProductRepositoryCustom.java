package com.pricewagon.pricewagon.domain.product.repository.product;

import java.util.List;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;

public interface ProductRepositoryCustom {
	List<Product> findProductsByShopTypeAndLastId(ShopType shopType, Integer lastId, int size);
}

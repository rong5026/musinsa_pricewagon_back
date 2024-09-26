package com.pricewagon.pricewagon.domain.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pricewagon.pricewagon.domain.category.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	List<Category> findAllByParentCategory_Id(Integer parentCateghoryId);
}

package com.pricewagon.pricewagon.domain.product.entity.product;

import org.hibernate.annotations.Comment;

import com.pricewagon.pricewagon.domain.category.entity.Category;
import com.pricewagon.pricewagon.domain.common.BaseAuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	@Comment("카테고리")
	private Category category;

	@Column(length = 200)
	@Comment("상품 이미지 URL")
	private String imgUrl;

	@Column(length = 100)
	@Comment("상품이름")
	private String name;

	@Column(length = 100)
	@Comment("브랜드 이름")
	private String brand;

	@Column(length = 200)
	@Comment("상품 URL")
	private String productUrl;

	@Comment("판매가격")
	private Integer salePrice;

	@Comment("원가")
	private Integer originalPrice;

}

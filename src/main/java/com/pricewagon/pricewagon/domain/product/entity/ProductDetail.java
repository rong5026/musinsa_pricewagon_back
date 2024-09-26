package com.pricewagon.pricewagon.domain.product.entity;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Comment("최고가")
	@Column(nullable = false)
	private Integer highPrice;

	@Comment("평균가")
	@Column(nullable = false)
	private Integer middlePrice;

	@Comment("최저가")
	@Column(nullable = false)
	private Integer lowPrice;

	@Comment("상품 URL")
	@Column(length = 200, nullable = false, unique = true)
	private String productUrl;
}

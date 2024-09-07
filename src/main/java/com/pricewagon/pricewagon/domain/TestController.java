package com.pricewagon.pricewagon.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pricewagon.pricewagon.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {
	private final CategoryService categoryService;

	@GetMapping("/")
	public String getParentAndSubCategories(

	) {
		return "환영한다";
	}
}

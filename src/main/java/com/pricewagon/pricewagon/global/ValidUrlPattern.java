package com.pricewagon.pricewagon.global;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = UrlPatternValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUrlPattern {
	String message() default "유효한 URL 형식이 아닙니다.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}

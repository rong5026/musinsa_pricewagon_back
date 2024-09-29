package com.pricewagon.pricewagon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PricewagonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PricewagonApplication.class, args);
	}

}

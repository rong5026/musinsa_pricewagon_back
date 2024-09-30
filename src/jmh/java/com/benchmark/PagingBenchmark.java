package com.benchmark;

import com.pricewagon.pricewagon.domain.product.entity.Product;
import com.pricewagon.pricewagon.domain.product.entity.ShopType;
import com.pricewagon.pricewagon.domain.product.repository.product.ProductRepository;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime) // 평균 시간 측정 모드
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 시간 단위 설정
@Warmup(iterations = 3) // 워밍업 설정
@Measurement(iterations = 5) // 실제 측정 횟수
@State(Scope.Benchmark) // 벤치마크 상태 정의
@Fork(1) // fork 수 설정
public class PagingBenchmark {

	private ProductRepository productRepository;
	private static final ShopType shopType = ShopType.MUSINSA;
	private static final int pageSize = 10;

	@Setup(Level.Trial)
	public void setUp() {
		ApplicationContext context = SpringApplication.run(PagingBenchmarkConfig.class);
		productRepository = context.getBean(ProductRepository.class);
	}

	// Offset 방식 첫페이지 조회 성능 테스트
	@Benchmark
	public void testOffsetFirstPaging(Blackhole blackhole) {
		Pageable pageable = PageRequest.of(0, pageSize);
		List<Product> offsetProducts = productRepository.findAllByShopType(shopType, pageable).getContent();
		blackhole.consume(offsetProducts);
	}

	// Offset 방식 마지막 페이지 조회 성능 테스트
	@Benchmark
	public void testOffsetLastPaging(Blackhole blackhole) {
		Pageable pageable = PageRequest.of(14999, pageSize);
		List<Product> offsetProducts = productRepository.findAllByShopType(shopType, pageable).getContent();
		blackhole.consume(offsetProducts);
	}

	// No-Offset 방식 마지막 페이지 조회 성능 테스트
	@Benchmark
	public void testNoOffsetPaging(Blackhole blackhole) {
		Integer lastId = 149990;
		List<Product> noOffsetProducts = productRepository.findProductsByShopTypeAndLastId(shopType, lastId, pageSize);
		blackhole.consume(noOffsetProducts);
	}
}
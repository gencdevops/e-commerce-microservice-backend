package com.fmss.basketservice;

import com.fmss.basketservice.feign.ProductClient;
import com.fmss.basketservice.service.BasketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@EnableFeignClients
@SpringBootApplication
public class BasketServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasketServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner basketServiceCommandLineRunner(BasketService basketService){
		return args -> {
			basketService.getBasketByUserId(UUID.randomUUID());
			String debug;
		};
	}

	@Bean
	CommandLineRunner productCommandLineRunner(ProductClient productClient){
		return args -> {
//			ProductResponseDto productById = productClient.getProductById(UUID.fromString("87c88ca0-4d6c-4f0e-a305-8070daf32c80"));
//			String debug;
		};
	}

}

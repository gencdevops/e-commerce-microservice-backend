package com.fmss.basketservice;

import com.fmss.basketservice.service.BasketService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class BasketServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasketServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BasketService basketService){
		return args -> {
			basketService.getBasketByUserId("myTestUser");
		};
	}

}

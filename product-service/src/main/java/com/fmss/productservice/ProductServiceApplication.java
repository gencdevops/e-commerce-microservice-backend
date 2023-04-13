package com.fmss.productservice;

import com.fmss.productservice.model.Product;
import com.fmss.productservice.model.dto.ProductResponseDto;
import com.fmss.productservice.repository.ProductRepository;
import com.fmss.productservice.service.ProductService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@EnableFeignClients
@EnableCaching
@EnableEncryptableProperties
@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ProductRepository productRepository, ProductService productService){
        return args -> {
//            productRepository.deleteByName("prod 1");
//            productRepository.deleteByName("prod 2");
            productRepository.deleteAll();

            Product product1 = Product.builder()
                    .url("https://advivemy-images.s3.us-east-2.amazonaws.com/a3434")
                    .price(BigDecimal.valueOf(1012.00))
                    .name("aaa")
                    .productId(UUID.fromString("209c0023-1305-46b4-afee-5a4f2f6cb5d8"))
//                    .changeDayLastTime(LocalDateTime.now())
//                    .createdDateTime(LocalDateTime.now())
                    .status(true)
                    .build();

            Product product2 = Product.builder()
                    .url("https://advivemy-images.s3.us-east-2.amazonaws.com/aa44")
                    .price(BigDecimal.valueOf(300.90))
                    .name("bbbb")
                    .productId(UUID.fromString("e6f1322c-9264-438d-b111-45fb1bbd5eee"))
//                    .changeDayLastTime(LocalDateTime.now())
//                    .createdDateTime(LocalDateTime.now())
                    .status(true)
                    .build();

            Product product3 = Product.builder()
                    .url("https://advivemy-images.s3.us-east-2.amazonaws.com/aa34")
                    .price(BigDecimal.valueOf(302.90))
                    .name("xxx")
                    .productId(UUID.fromString("b68ac92b-40f6-4098-96c8-815f7e8f10b2"))
//                    .changeDayLastTime(LocalDateTime.now())
//                    .createdDateTime(LocalDateTime.now())
                    .status(true)
                    .build();

            productRepository.saveAllAndFlush(List.of(product1, product2, product3));

            //List<ProductResponseDto> allProducts1 = productService.getAllProducts();
            System.out.println();
        };
    }

}

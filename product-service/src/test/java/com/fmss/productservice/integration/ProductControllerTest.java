package com.fmss.productservice.integration;

import com.fmss.commondata.model.enums.PaymentStatus;
import com.fmss.productservice.model.Product;
import com.fmss.productservice.repository.ProductRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.fmss.productservice.constants.ProductConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private Product alreadyCreatedProduct;

    @BeforeEach
    void setUp() {
        alreadyCreatedProduct = Product.builder()
                .url("https://advivemy-images.s3.us-east-2.amazonaws.com/a3434")
                .price(BigDecimal.valueOf(1012.00))
                .name("aaa")
                .productId(UUID.randomUUID())
                .status(true)
                .build();

        productRepository.save(alreadyCreatedProduct);
    }
    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void getAllProducts() throws Exception{
        this.mockMvc.perform(get(API_PREFIX + API_VERSION_V1 + API_PRODUCTS)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.not(0)));
    }

    @Test
    void getProductById() throws Exception{
        this.mockMvc.perform(get(API_PREFIX + API_VERSION_V1 + API_PRODUCTS + "/" + "olmayan-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

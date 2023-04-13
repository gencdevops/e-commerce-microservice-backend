package com.fmss.productservice.integration;

import com.fmss.commondata.model.enums.PaymentStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.fmss.productservice.constants.ProductConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest extends BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getAllProducts() throws Exception{

        this.mockMvc.perform(get(API_PREFIX + API_VERSION_V1 + API_PRODUCTS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId", Matchers.notNullValue()))
                .andExpect(jsonPath("$.paymentStatus").value(PaymentStatus.PENDING.name())
                );

//        this.mockMvc.perform(post(API_PREFIX + API_VERSION_V1 + API_PAYMENTS + API_PLACE_PAYMENT)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsBytes(createPaymentRequestDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.paymentId", Matchers.notNullValue()))
//                .andExpect(jsonPath("$.paymentStatus").value(PaymentStatus.PENDING.name())
//                );
    }
}

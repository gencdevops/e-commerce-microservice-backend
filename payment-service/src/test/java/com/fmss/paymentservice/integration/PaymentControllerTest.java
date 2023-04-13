package com.fmss.paymentservice.integration;

import com.fmss.commondata.dtos.request.CreatePaymentRequestDto;
import com.fmss.commondata.model.enums.PaymentStatus;
import com.fmss.paymentservice.repository.PaymentRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.fmss.paymentservice.constants.PaymentConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest extends BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPayment() throws Exception {
        CreatePaymentRequestDto createPaymentRequestDto = CreatePaymentRequestDto.builder()
                .orderId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();

        this.mockMvc.perform(post(API_PREFIX + API_VERSION_V1 + API_PAYMENTS + API_PLACE_PAYMENT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createPaymentRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId", Matchers.notNullValue()))
                .andExpect(jsonPath("$.paymentStatus").value(PaymentStatus.PENDING.name())
                );
    }
}

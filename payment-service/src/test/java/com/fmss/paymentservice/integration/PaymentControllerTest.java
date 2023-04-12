package com.fmss.paymentservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fmss.paymentservice.model.dto.CreatePaymentRequestDto;
import com.fmss.paymentservice.model.entity.Payment;
import com.fmss.paymentservice.model.enums.PaymentStatus;
import com.fmss.paymentservice.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentControllerTest extends BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    private Payment alreadyExistPayment;

    private CreatePaymentRequestDto createPaymentRequestDto;

    @BeforeEach
    void setUp() {
        String paymentId = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        paymentRepository.save(
                Payment.builder()
                        .createdBy("admin-test")
                        .createdDate(ZonedDateTime.now())
                        .id(paymentId)
                        .orderId(orderId)
                        .userId(userId)
                        .paymentStatus(PaymentStatus.PENDING)
                        .build()
        );


    }

    @Test
    void createPayment() throws Exception {
        createPaymentRequestDto = CreatePaymentRequestDto.builder()
                .orderId(UUID.randomUUID().toString())
                .userId(UUID.randomUUID().toString())
                .build();

        this.mockMvc.perform(post(BASE_PAYMENT_API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(createPaymentRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value(createPaymentRequestDto.orderId()))
                .andExpect(jsonPath("$.productStatus").value(createPaymentRequestDto.userId()));
    }
}

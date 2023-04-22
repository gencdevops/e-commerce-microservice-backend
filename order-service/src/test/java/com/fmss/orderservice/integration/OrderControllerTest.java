package com.fmss.orderservice.integration;

import com.fmss.commondata.dtos.response.BasketItemResponseDto;
import com.fmss.commondata.dtos.response.BasketResponseDto;
import com.fmss.commondata.model.enums.PaymentStatus;
import com.fmss.orderservice.dto.PlaceOrderRequestDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.fmss.orderservice.constants.OrderConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void placeOrder() throws Exception{
        BasketItemResponseDto basketItemResponseDto = BasketItemResponseDto.builder()
                .basketItemId(UUID.randomUUID())
                .name("Süt")
                .imgUrl("https://advivemy-images.s3.us-east-2.amazonaws.com/sut")
                .productId(UUID.randomUUID())
                .quantity(21)
                .build();

        BasketResponseDto basketResponseDto = BasketResponseDto.builder()
                .basketId(UUID.randomUUID())
                .basketItemList(List.of(basketItemResponseDto))
                .totalPrice(BigDecimal.valueOf(1000))
                .build();

        PlaceOrderRequestDTO placeOrderRequestDTO = PlaceOrderRequestDTO.builder()
                .userId(UUID.randomUUID())
                .basketResponseDto(basketResponseDto)
                .build();

        this.mockMvc.perform(post(API_PREFIX + API_VERSION_V1 + API_ORDER )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(placeOrderRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId", Matchers.notNullValue()))
                .andExpect(jsonPath("$.totalPrice").value(basketResponseDto.totalPrice()))
                .andExpect(jsonPath("$.orderStatus").value(PaymentStatus.APPROVAL));
    }
}

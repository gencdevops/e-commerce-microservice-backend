package com.fmss.paymentservice.controller;


import com.fmss.commondata.dtos.request.CreatePaymentRequestDto;
import com.fmss.commondata.dtos.response.PaymentResponseDto;
import com.fmss.paymentservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.fmss.paymentservice.constants.PaymentConstants.*;

@RestController
@RequestMapping(API_PREFIX + API_VERSION_V1 + API_PAYMENTS)
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    public final PaymentService paymentService;

    @Operation(summary = "Create payment")
    @ApiResponses(value =
    @ApiResponse(
            responseCode = "201",
            description = "Place payment",
            content = @Content(
                    schema = @Schema(implementation = PaymentResponseDto.class),
                    mediaType = "application/json")))
    @PostMapping(API_PLACE_PAYMENT)
    @ResponseStatus(value = HttpStatus.CREATED)
    public PaymentResponseDto createPayment(@RequestBody @Valid CreatePaymentRequestDto createPaymentRequestDto) {
        return paymentService.createPayment(createPaymentRequestDto);
    }
}

package com.fmss.orderservice.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "order_outbox")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderOutboxId;
    @Lob
    private String orderPayload;
    private UUID orderId;
    private String paymentId;



}

package com.fmss.orderservice.model;

import com.fmss.orderservice.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;


    private BigDecimal totalPrice;


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;



    @CreationTimestamp
    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDateTime;


    @Column(columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime changeDayLastTime;
}

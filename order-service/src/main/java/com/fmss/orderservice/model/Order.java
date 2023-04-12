package com.fmss.orderservice.model;

import com.fmss.commondata.model.AbstractEntity;
import com.fmss.commondata.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "orders")
public class Order extends AbstractEntity implements Serializable {

    private BigDecimal totalPrice;


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

}

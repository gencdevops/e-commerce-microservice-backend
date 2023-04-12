package com.fmss.paymentservice.model.entity;


import com.fmss.commondata.model.AbstractEntity;
import com.fmss.commondata.model.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends AbstractEntity implements Serializable {
    private String orderId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String userId;
}

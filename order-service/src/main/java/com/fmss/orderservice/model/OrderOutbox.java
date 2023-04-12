package com.fmss.orderservice.model;


import com.fmss.commondata.model.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OrderOutbox extends AbstractEntity implements Serializable {

    @Lob
    private String orderPayload;

    private UUID orderId;

    private String paymentId;

}

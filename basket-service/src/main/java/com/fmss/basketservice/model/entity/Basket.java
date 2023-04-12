package com.fmss.basketservice.model.entity;

import com.fmss.basketservice.model.enums.BasketStatus;
import com.fmss.commondata.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Basket extends AbstractEntity {

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BasketItem> basketItems;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private BasketStatus basketStatus;

    private UUID userId;
}

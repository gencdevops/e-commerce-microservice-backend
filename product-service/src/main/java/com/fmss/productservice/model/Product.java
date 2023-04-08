package com.fmss.productservice.model;


import com.fmss.commondata.model.entity.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Product extends AbstractEntity {
    private String name;
    private BigDecimal price;
    private boolean status;
}

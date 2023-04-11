package com.fmss.productservice.model;


import com.fmss.commondata.model.entity.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AbstractEntity {
    private String name;
    private BigDecimal price;
    private Boolean status;
    private String url;
}

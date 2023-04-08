package com.fmss.userservice.model.entity;

import com.fmss.commondata.model.entity.AbstractEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AbstractEntity {
    private String name;
}

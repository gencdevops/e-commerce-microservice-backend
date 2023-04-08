package com.fmss.commondata.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners({AuditingEntityListener.class})
public class AbstractEntity extends IdEntity {

    @CreatedDate
    @Column(name = "created_date", nullable = true, updatable = false)
    protected ZonedDateTime createdDate = ZonedDateTime.now();

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    protected ZonedDateTime lastModifiedDate = ZonedDateTime.now();

    @CreatedBy
    @Column(name = "created_by", nullable = true, updatable = false)
    protected String createdBy;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    protected String lastModifiedBy;
}

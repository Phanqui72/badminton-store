package com.badminton.store.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "coupon")
@Getter @Setter
public class Coupon extends Auditable<String> {
    @Column(unique = true, nullable = false)
    private String code;
    private Double discountValue;
    private Integer discountType;
    private Integer limitUsage;
    private Integer used = 0;
    private LocalDateTime expiredDate;
}
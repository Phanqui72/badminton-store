package com.badminton.store.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@PrimaryKeyJoinColumn(name = "account_id")
@Table(name = TablePrefix.PREFIX_TABLE + "seller")
public class Seller extends Account{
    @Column(name = "shop_name")
    private String shopName;
    @Column(name = "shop_description", columnDefinition = "TEXT")
    private String shopDescription;
//    @Column(name = "pickup_address")
//    private String pickupAddress;
//    @Column(name = "bank_details", columnDefinition = "TEXT")
//    private String bankDetails;
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;
    private String gstIn;
}

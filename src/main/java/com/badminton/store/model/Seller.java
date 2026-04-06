package com.badminton.store.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = TablePrefix.PREFIX_TABLE + "seller")
public class Seller extends Auditable<String>{
    @Id
    private Long id;

    @OneToOne
    @MapsId // Ánh xạ ID của Account sang ID của Seller
    @JoinColumn(name = "id") // Tên cột khóa ngoại trong bảng seller cũng là id
    private Account account;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "shop_description", columnDefinition = "TEXT")
    private String shopDescription;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    private String gstIn;
}

package com.badminton.store.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "order_detail")
@Getter
@Setter
public class OrderDetail extends Auditable<String> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private Double price; // Giá tại thời điểm mua (Fixed price)
}
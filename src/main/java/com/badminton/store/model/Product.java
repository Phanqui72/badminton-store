package com.badminton.store.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "product")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public class Product extends Auditable<String> {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;

    @ElementCollection
    @CollectionTable(name = TablePrefix.PREFIX_TABLE + "product_images",
            joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_path")
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;
}
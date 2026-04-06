package com.badminton.store.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = TablePrefix.PREFIX_TABLE + "category")
public class Category extends Auditable<String>{
    private String name;

    @Column(name = "description", columnDefinition = "Text" )//biến đổi thành kiểu Text
    private String description;
}
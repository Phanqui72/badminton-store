package com.badminton.store.model;

import com.badminton.store.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "user")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Account {

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender; // dung interger

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Address> address;

}

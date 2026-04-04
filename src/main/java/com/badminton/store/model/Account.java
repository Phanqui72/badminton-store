package com.badminton.store.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
// add annatation to mapping inheritance and i want add a account_id feild on seller table
// JOINED: 1 class is 1 table, when we query it will join 2 table (only query not  crate table)
// SINGLE_TABLE : all field in 1 table
// TABLE_PER_CLASS: 1 Parent table will contain all supper class field
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = TablePrefix.PREFIX_TABLE + "account")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account extends Auditable<String> {
    private int kind;
    private String username;
    private String phone;
    private String email;
    @JsonIgnore
    private String password;
    @Column(name = "full_name")
    private String fullName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
    @Column(name = "last_login")
    private Date lastLogin;
    @Column(name = "avatar_path")
    private String avatarPath;
    @Column(name = "reset_pwd_code")
    private String resetPwdCode;
    @Column(name = "reset_pwd_time")
    private Date resetPwdTime;
    @Column(name = "attempt_forget_pwd")
    private Integer attemptCode;
    @Column(name = "attempt_login")
    private Integer attemptLogin;
    @Column(name = "is_super_admin")
    private Boolean isSuperAdmin = false;
}

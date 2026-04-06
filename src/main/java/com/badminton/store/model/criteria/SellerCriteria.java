package com.badminton.store.model.criteria;

import com.badminton.store.model.Account;
import com.badminton.store.model.Seller;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class SellerCriteria {
    private Long id;
    private String shopName;
    private String username;
    private String email;
    private String phone;
    private Integer status;

    public Specification<Seller> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }

            if (StringUtils.isNotBlank(shopName)) {
                predicates.add(cb.like(cb.lower(root.get("shopName")), "%" + shopName.toLowerCase() + "%"));
            }

            // Lọc các trường thuộc bảng Account (Join)
            if (StringUtils.isNotBlank(username) || StringUtils.isNotBlank(email) || StringUtils.isNotBlank(phone) || status != null) {
                Join<Seller, Account> accountJoin = root.join("account");

                if (StringUtils.isNotBlank(username)) {
                    predicates.add(cb.like(cb.lower(accountJoin.get("username")), "%" + username.toLowerCase() + "%"));
                }
                if (StringUtils.isNotBlank(email)) {
                    predicates.add(cb.like(cb.lower(accountJoin.get("email")), "%" + email.toLowerCase() + "%"));
                }
                if (StringUtils.isNotBlank(phone)) {
                    predicates.add(cb.like(accountJoin.get("phone"), "%" + phone + "%"));
                }
                if (status != null) {
                    predicates.add(cb.equal(accountJoin.get("status"), status));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
package com.badminton.store.repository;

import com.badminton.store.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon,Long>, JpaSpecificationExecutor<Coupon> {
    Optional<Coupon> findFirstByCodeAndStatus(String code, Integer status);
}

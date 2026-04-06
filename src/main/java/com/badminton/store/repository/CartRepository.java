package com.badminton.store.repository;

import com.badminton.store.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CartRepository extends JpaRepository<Cart,Long>, JpaSpecificationExecutor<Cart> {
}

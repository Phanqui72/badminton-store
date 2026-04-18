package com.badminton.store.repository;

import com.badminton.store.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    Optional<Account> findFirstByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndIdNot(String email, Long id);

    Boolean existsByPhone(String phone);

    Boolean existsByPhoneAndIdNot(String phone, Long id);

    Optional<Account> findFirstByEmail(String email);

    @Query("SELECT a FROM Account a WHERE a.username = :username OR a.phone = :phone")
    Optional<Account> findByUsernameOrPhone(@Param("username") String username, @Param("phone") String phone);

    Optional<Account> findByIdAndStatus(long id, Integer status);
}

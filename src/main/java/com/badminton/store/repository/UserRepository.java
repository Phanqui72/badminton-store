package com.badminton.store.repository;

import com.badminton.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findFirstByUsername(@NotEmpty(message = "Username is required") @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters") String username);
}

package com.badminton.store.repository;

import com.badminton.store.model.Nation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NationRepository extends JpaRepository<Nation, Long>, JpaSpecificationExecutor<Nation> {
}

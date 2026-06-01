package com.walmir.tmaoranking.repository;

import com.walmir.tmaoranking.domain.Kit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitRepository extends JpaRepository<Kit, Long> {
}
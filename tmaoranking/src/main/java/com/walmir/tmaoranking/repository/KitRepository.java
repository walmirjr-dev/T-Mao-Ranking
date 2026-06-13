package com.walmir.tmaoranking.repository;

import com.walmir.tmaoranking.domain.Kit;
import com.walmir.tmaoranking.domain.enums.KitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KitRepository extends JpaRepository<Kit, Long> {

    boolean existsByNameAndKitTypeAndReleaseYear(String name, KitType kitType, Integer releaseYear);

    boolean existsByKitTypeAndReleaseYear(KitType kitType, Integer releaseYear);

    List<Kit> findByReleaseYear(Integer releaseYear);

    List<Kit> findByReleaseYearBetween(Integer startYear, Integer endYear);

    List<Kit> findByKitType(KitType kitType);

    List<Kit> findByNameContainingIgnoreCase(String name);
}
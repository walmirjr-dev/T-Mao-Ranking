package com.walmir.tmaoranking.repository;

import com.walmir.tmaoranking.domain.Kit;
import com.walmir.tmaoranking.domain.enums.KitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitRepository extends JpaRepository<Kit, Long> {

    boolean existsByNameAndKitTypeAndReleaseYear(String name, KitType kitType, Integer releaseYear);

    boolean existsByKitTypeAndReleaseYear(KitType kitType, Integer releaseYear);

}
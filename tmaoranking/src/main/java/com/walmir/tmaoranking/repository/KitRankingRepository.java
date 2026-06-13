package com.walmir.tmaoranking.repository;

import com.walmir.tmaoranking.domain.KitRanking;
import com.walmir.tmaoranking.domain.KitRankingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitRankingRepository extends JpaRepository<KitRanking, KitRankingKey> {
}
package com.walmir.tmaoranking.repository;

import com.walmir.tmaoranking.domain.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    List<Ranking> findByUserId(Long userId);
}

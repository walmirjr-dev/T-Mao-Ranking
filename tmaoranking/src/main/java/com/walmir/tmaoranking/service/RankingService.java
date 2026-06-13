package com.walmir.tmaoranking.service;

import com.walmir.tmaoranking.domain.Kit;
import com.walmir.tmaoranking.domain.KitRanking;
import com.walmir.tmaoranking.domain.KitRankingKey;
import com.walmir.tmaoranking.domain.Ranking;
import com.walmir.tmaoranking.repository.KitRankingRepository;
import com.walmir.tmaoranking.repository.KitRepository;
import com.walmir.tmaoranking.repository.RankingRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RankingService {

    private final RankingRepository rankingRepository;
    private final KitRepository kitRepository;
    private final KitRankingRepository kitRankingRepository;

    public RankingService(RankingRepository rankingRepository, KitRepository kitRepository, KitRankingRepository kitRankingRepository) {
        this.rankingRepository = rankingRepository;
        this.kitRepository = kitRepository;
        this.kitRankingRepository = kitRankingRepository;
    }

    @Transactional
    public Ranking insert(Ranking ranking) {
        if (ranking.getKitRankings() == null) {
            ranking.setKitRankings(new ArrayList<>());
        }
        ranking.setCreatedAt(Instant.now());
        validateRanking(ranking);
        return rankingRepository.save(ranking);
    }

    public List<Ranking> findAll() {
        return rankingRepository.findAll();
    }

    public Ranking findById(Long id) {
        return rankingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ranking not found: " + id));
    }

    @Transactional
    public Ranking update(Long id, Ranking ranking) {
        Ranking existing = findById(id);
        existing.setTitle(ranking.getTitle());
        existing.setRankingType(ranking.getRankingType());
        existing.setKitRankings(ranking.getKitRankings());
        validateRanking(existing);
        return rankingRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!rankingRepository.existsById(id)) {
            throw new RuntimeException("Ranking not found: " + id);
        }
        try {
            rankingRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Database integrity violation: " + e.getMessage());
        }
    }


    @Transactional
    public Ranking addKit(Long rankingId, Long kitId, Integer position) {

        Ranking ranking = findById(rankingId);

        Kit kit = kitRepository.findById(kitId)
                .orElseThrow(() -> new RuntimeException("Kit not found"));

        KitRankingKey key = new KitRankingKey(rankingId, kitId);

        KitRanking kitRanking = new KitRanking();
        kitRanking.setId(key);
        kitRanking.setRanking(ranking);
        kitRanking.setKit(kit);
        kitRanking.setPosition(position);

        ranking.getKitRankings().add(kitRanking);

        validateRanking(ranking);

        rankingRepository.save(ranking);

        return ranking;
    }

    @Transactional
    public void removeKit(Long rankingId, Long kitId) {
        Ranking ranking = findById(rankingId);
        ranking.getKitRankings().removeIf(kr -> kr.getKit().getId().equals(kitId));
        rankingRepository.save(ranking);
    }

    private void validateRanking(Ranking ranking) {
        validateSize(ranking);
        validateDuplicatePositions(ranking);
        validateDuplicateKits(ranking);
    }

    private void validateSize(Ranking ranking) {
        int max = ranking.getRankingType().getMaxSize();
        if (ranking.getKitRankings().size() > max) {
            throw new RuntimeException("Ranking exceeded max size: " + max);
        }
    }

    private void validateDuplicatePositions(Ranking ranking) {
        List<Integer> positions = new ArrayList<>();
        for (KitRanking kr : ranking.getKitRankings()) {
            if (positions.contains(kr.getPosition())) {
                throw new RuntimeException("Duplicate position in ranking: " + kr.getPosition());
            }
            positions.add(kr.getPosition());
        }
    }

    private void validateDuplicateKits(Ranking ranking) {
        List<Long> kitIds = new ArrayList<>();
        for (KitRanking kr : ranking.getKitRankings()) {
            Long kitId = kr.getKit().getId();
            if (kitIds.contains(kitId)) {
                throw new RuntimeException("Duplicate kit in ranking: " + kitId);
            }
            kitIds.add(kitId);
        }
    }

    
}
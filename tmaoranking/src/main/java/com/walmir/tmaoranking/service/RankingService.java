package com.walmir.tmaoranking.service;

import com.walmir.tmaoranking.domain.*;
import com.walmir.tmaoranking.dto.request.KitRankingRequest;
import com.walmir.tmaoranking.dto.request.RankingRequest;
import com.walmir.tmaoranking.dto.response.RankingDetailResponse;
import com.walmir.tmaoranking.dto.response.RankingSummaryResponse;
import com.walmir.tmaoranking.exception.BusinessException;
import com.walmir.tmaoranking.exception.DatabaseException;
import com.walmir.tmaoranking.exception.ResourceNotFoundException;
import com.walmir.tmaoranking.repository.KitRepository;
import com.walmir.tmaoranking.repository.RankingRepository;
import com.walmir.tmaoranking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RankingService {

    private final RankingRepository rankingRepository;
    private final UserRepository userRepository;
    private final KitRepository kitRepository;

    public RankingService(
            RankingRepository rankingRepository,
            UserRepository userRepository,
            KitRepository kitRepository) {

        this.rankingRepository = rankingRepository;
        this.userRepository = userRepository;
        this.kitRepository = kitRepository;
    }

    public RankingSummaryResponse insert(RankingRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(request.userId()));

        Ranking ranking = new Ranking();

        ranking.setTitle(request.title());
        ranking.setRankingType(request.rankingType());
        ranking.setUser(user);
        ranking.setCreatedAt(Instant.now());
        ranking.setKitRankings(new ArrayList<>());

        validateRanking(ranking);

        return RankingSummaryResponse.from(
                rankingRepository.save(ranking));
    }

    public List<RankingSummaryResponse> findAll() {

        return rankingRepository.findAll()
                .stream()
                .map(RankingSummaryResponse::from)
                .toList();
    }

    public RankingDetailResponse findById(Long id) {

        return RankingDetailResponse.from(getRanking(id));
    }

    public RankingDetailResponse update(
            Long id,
            RankingRequest request) {

        Ranking ranking = getRanking(id);

        ranking.setTitle(request.title());
        ranking.setRankingType(request.rankingType());

        validateRanking(ranking);

        return RankingDetailResponse.from(
                rankingRepository.save(ranking));
    }

    public void delete(Long id) {
        if (!rankingRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        try {
            rankingRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public RankingDetailResponse addKit(
            Long rankingId,
            KitRankingRequest request) {

        Ranking ranking = getRanking(rankingId);

        Kit kit = kitRepository.findById(request.kitId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(request.kitId()));

        KitRankingKey key =
                new KitRankingKey(
                        rankingId,
                        request.kitId());

        KitRanking kitRanking = new KitRanking();

        kitRanking.setId(key);
        kitRanking.setRanking(ranking);
        kitRanking.setKit(kit);
        kitRanking.setPosition(request.position());

        ranking.getKitRankings().add(kitRanking);

        validateRanking(ranking);

        rankingRepository.save(ranking);

        return RankingDetailResponse.from(ranking);
    }

    public RankingDetailResponse removeKit(
            Long rankingId,
            Long kitId) {

        Ranking ranking = getRanking(rankingId);

        ranking.getKitRankings()
                .removeIf(
                        kr -> kr.getKit()
                                .getId()
                                .equals(kitId));

        rankingRepository.save(ranking);

        return RankingDetailResponse.from(ranking);
    }

    private Ranking getRanking(Long id) {

        return rankingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(id));
    }

    private void validateRanking(Ranking ranking) {
        validateSize(ranking);
        validateDuplicatePositions(ranking);
        validateDuplicateKits(ranking);
    }

    private void validateSize(Ranking ranking) {

        int max = ranking.getRankingType().getMaxSize();

        if (ranking.getKitRankings().size() > max) {
            throw new BusinessException(
                    "Ranking exceeded max size: " + max);
        }
    }

    private void validateDuplicatePositions(Ranking ranking) {

        List<Integer> positions = new ArrayList<>();

        for (KitRanking kr : ranking.getKitRankings()) {

            if (positions.contains(kr.getPosition())) {
                throw new BusinessException(
                        "Duplicate position in ranking: "
                                + kr.getPosition());
            }

            positions.add(kr.getPosition());
        }
    }

    private void validateDuplicateKits(Ranking ranking) {

        List<Long> kitIds = new ArrayList<>();

        for (KitRanking kr : ranking.getKitRankings()) {

            Long kitId = kr.getKit().getId();

            if (kitIds.contains(kitId)) {
                throw new BusinessException(
                        "Duplicate kit in ranking: "
                                + kitId);
            }

            kitIds.add(kitId);
        }
    }
}
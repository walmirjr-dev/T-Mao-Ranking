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
import com.walmir.tmaoranking.security.AuthenticatedUserService;
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
    private final KitRepository kitRepository;
    private final AuthenticatedUserService authService;

    public RankingService(
            RankingRepository rankingRepository,
            KitRepository kitRepository,
            AuthenticatedUserService authService) {
        this.rankingRepository = rankingRepository;
        this.kitRepository = kitRepository;
        this.authService = authService;
    }

    public RankingSummaryResponse insert(RankingRequest request) {
        User loggedUser = authService.getLoggedUser();

        Ranking ranking = new Ranking();
        ranking.setTitle(request.title());
        ranking.setRankingType(request.rankingType());
        ranking.setUser(loggedUser);
        ranking.setCreatedAt(Instant.now());
        ranking.setKitRankings(new ArrayList<>());

        validateRanking(ranking);

        return RankingSummaryResponse.from(rankingRepository.save(ranking));
    }

    public List<RankingSummaryResponse> findAll() {
        User loggedUser = authService.getLoggedUser();
        return rankingRepository.findByUserId(loggedUser.getId())
                .stream()
                .map(RankingSummaryResponse::from)
                .toList();
    }

    public RankingDetailResponse findById(Long id) {
        Ranking ranking = getRanking(id);
        authService.checkOwnership(ranking.getUser().getId());
        return RankingDetailResponse.from(ranking);
    }

    public RankingDetailResponse update(Long id, RankingRequest request) {
        Ranking ranking = getRanking(id);
        authService.checkOwnership(ranking.getUser().getId());

        ranking.setTitle(request.title());
        ranking.setRankingType(request.rankingType());

        validateRanking(ranking);

        return RankingDetailResponse.from(rankingRepository.save(ranking));
    }

    public void delete(Long id) {
        Ranking ranking = getRanking(id);
        authService.checkOwnership(ranking.getUser().getId());
        try {
            rankingRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public RankingDetailResponse addKit(Long rankingId, KitRankingRequest request) {
        Ranking ranking = getRanking(rankingId);
        authService.checkOwnership(ranking.getUser().getId());

        Kit kit = kitRepository.findById(request.kitId())
                .orElseThrow(() -> new ResourceNotFoundException(request.kitId()));

        KitRankingKey key = new KitRankingKey(rankingId, request.kitId());
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

    public RankingDetailResponse removeKit(Long rankingId, Long kitId) {
        Ranking ranking = getRanking(rankingId);
        authService.checkOwnership(ranking.getUser().getId());

        ranking.getKitRankings().removeIf(kr -> kr.getKit().getId().equals(kitId));
        rankingRepository.save(ranking);

        return RankingDetailResponse.from(ranking);
    }

    private Ranking getRanking(Long id) {
        return rankingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
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

    @Transactional
    public RankingDetailResponse updateRankingTitle(Long id, String newTitle) {
        Ranking ranking = getRanking(id);

        ranking.setTitle(newTitle);
        rankingRepository.save(ranking);

        return RankingDetailResponse.from(ranking);
    }
}
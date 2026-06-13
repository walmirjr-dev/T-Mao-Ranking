package com.walmir.tmaoranking.dto.response;

import com.walmir.tmaoranking.domain.Ranking;
import com.walmir.tmaoranking.domain.enums.RankingType;

import java.time.Instant;
import java.util.List;

public record RankingDetailResponse(
        Long id,
        String title,
        RankingType rankingType,
        Instant createdAt,
        List<KitRankingResponse> kitRankings
) {
    public static RankingDetailResponse from(Ranking ranking) {
        return new RankingDetailResponse(
                ranking.getId(),
                ranking.getTitle(),
                ranking.getRankingType(),
                ranking.getCreatedAt(),
                ranking.getKitRankings().stream()
                        .map(KitRankingResponse::from)
                        .toList()
        );
    }
}
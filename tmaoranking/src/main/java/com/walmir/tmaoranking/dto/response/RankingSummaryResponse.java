package com.walmir.tmaoranking.dto.response;

import com.walmir.tmaoranking.domain.Ranking;
import com.walmir.tmaoranking.domain.enums.RankingType;

import java.time.Instant;

public record RankingSummaryResponse(
        Long id,
        String title,
        RankingType rankingType,
        Instant createdAt
) {
    public static RankingSummaryResponse from(Ranking ranking) {
        return new RankingSummaryResponse(
                ranking.getId(),
                ranking.getTitle(),
                ranking.getRankingType(),
                ranking.getCreatedAt()
        );
    }
}
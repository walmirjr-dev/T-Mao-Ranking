package com.walmir.tmaoranking.dto.response;

import com.walmir.tmaoranking.domain.KitRanking;

public record KitRankingResponse(
        Integer position,
        KitResponse kit
) {
    public static KitRankingResponse from(KitRanking kitRanking) {
        return new KitRankingResponse(
                kitRanking.getPosition(),
                KitResponse.from(kitRanking.getKit())
        );
    }
}
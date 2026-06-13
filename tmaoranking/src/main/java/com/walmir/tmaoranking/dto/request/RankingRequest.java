package com.walmir.tmaoranking.dto.request;

import com.walmir.tmaoranking.domain.enums.RankingType;

public record RankingRequest(
        String title,
        RankingType rankingType,
        Long userId
) {}
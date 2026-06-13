package com.walmir.tmaoranking.dto.request;

import com.walmir.tmaoranking.domain.enums.KitType;

public record KitRequest(
        Integer releaseYear,
        String name,
        KitType kitType,
        String imgUrl,
        String description
) {}
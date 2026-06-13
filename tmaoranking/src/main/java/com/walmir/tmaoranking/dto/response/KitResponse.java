package com.walmir.tmaoranking.dto.response;

import com.walmir.tmaoranking.domain.Kit;
import com.walmir.tmaoranking.domain.enums.KitType;

public record KitResponse(
        Long id,
        Integer releaseYear,
        String name,
        KitType kitType,
        String imgUrl,
        String description
) {
    public static KitResponse from(Kit kit) {
        return new KitResponse(
                kit.getId(),
                kit.getReleaseYear(),
                kit.getName(),
                kit.getKitType(),
                kit.getImgUrl(),
                kit.getDescription()
        );
    }
}
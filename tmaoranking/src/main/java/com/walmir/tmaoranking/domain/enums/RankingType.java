package com.walmir.tmaoranking.domain.enums;

public enum RankingType {
    TOP_3(3),
    TOP_5(5),
    TOP_10(10);

    private final int maxSize;

    RankingType(int maxSize) {
        this.maxSize = maxSize;
    }
    public int getMaxSize() {
        return maxSize;
    }
}

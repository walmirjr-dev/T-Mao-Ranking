package com.walmir.tmaoranking.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KitRankingKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "ranking_id")
    private Long rankingId;

    @Column(name = "kit_id")
    private Long kitId;
}
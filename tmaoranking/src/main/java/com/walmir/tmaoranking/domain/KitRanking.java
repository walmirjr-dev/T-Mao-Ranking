package com.walmir.tmaoranking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "kit_rankings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KitRanking implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private KitRankingKey id;

    @ManyToOne
    @MapsId("rankingId")
    @JoinColumn(name = "ranking_id")
    @JsonIgnore
    private Ranking ranking;

    @ManyToOne
    @MapsId("kitId")
    @JoinColumn(name = "kit_id")
    private Kit kit;

    @Column(name = "position", nullable = false)
    private Integer position;
}
package com.walmir.tmaoranking.domain;

import com.walmir.tmaoranking.domain.enums.RankingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rankings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ranking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title is required")
    @Column(name = "title")
    private String title;

    @NotNull(message = "Ranking type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "ranking_type")
    private RankingType rankingType;

    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "ranking")
    private List<KitRanking> kitRankings = new ArrayList<>();


}

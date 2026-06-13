package com.walmir.tmaoranking.controller;

import com.walmir.tmaoranking.domain.KitRanking;
import com.walmir.tmaoranking.domain.Ranking;
import com.walmir.tmaoranking.dto.request.KitRankingRequest;
import com.walmir.tmaoranking.dto.request.RankingRequest;
import com.walmir.tmaoranking.dto.response.RankingDetailResponse;
import com.walmir.tmaoranking.dto.response.RankingSummaryResponse;
import com.walmir.tmaoranking.service.RankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/rankings")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @PostMapping
    public ResponseEntity<RankingSummaryResponse> insert(
            @RequestBody RankingRequest request) {

        RankingSummaryResponse saved =
                rankingService.insert(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(saved);
    }

    @GetMapping
    public ResponseEntity<List<RankingSummaryResponse>> findAll() {

        return ResponseEntity.ok(
                rankingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RankingDetailResponse> findById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                rankingService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RankingDetailResponse> update(
            @PathVariable Long id,
            @RequestBody RankingRequest request) {

        return ResponseEntity.ok(
                rankingService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        rankingService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/kits")
    public ResponseEntity<RankingDetailResponse> addKit(
            @PathVariable Long id,
            @RequestBody KitRankingRequest request) {

        return ResponseEntity.ok(
                rankingService.addKit(id, request));
    }

    @DeleteMapping("/{id}/kits/{kitId}")
    public ResponseEntity<RankingDetailResponse> removeKit(
            @PathVariable Long id,
            @PathVariable Long kitId) {

        return ResponseEntity.ok(
                rankingService.removeKit(id, kitId));
    }
}
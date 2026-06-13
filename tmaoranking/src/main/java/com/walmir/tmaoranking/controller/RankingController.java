package com.walmir.tmaoranking.controller;

import com.walmir.tmaoranking.domain.KitRanking;
import com.walmir.tmaoranking.domain.Ranking;
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
    public ResponseEntity<Ranking> insert(@RequestBody Ranking ranking) {
        Ranking saved = rankingService.insert(ranking);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Ranking>> findAll() {
        return ResponseEntity.ok(rankingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ranking> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rankingService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ranking> update(@PathVariable Long id, @RequestBody Ranking ranking) {
        return ResponseEntity.ok(rankingService.update(id, ranking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rankingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/kits")
    public ResponseEntity<Ranking> addKit(@PathVariable Long id, @RequestBody KitRanking kitRanking) {
        return ResponseEntity.ok(rankingService.addKit(id, kitRanking.getId().getKitId(), kitRanking.getPosition()));
    }
    @DeleteMapping("/{id}/kits/{kitId}")
    public ResponseEntity<Void> removeKit(@PathVariable Long id, @PathVariable Long kitId) {
        rankingService.removeKit(id, kitId);
        return ResponseEntity.noContent().build();
    }
}
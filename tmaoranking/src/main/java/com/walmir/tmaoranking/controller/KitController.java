package com.walmir.tmaoranking.controller;

import com.walmir.tmaoranking.domain.Kit;
import com.walmir.tmaoranking.dto.request.KitRequest;
import com.walmir.tmaoranking.dto.response.KitResponse;
import com.walmir.tmaoranking.service.KitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/kits")
public class KitController {

    private final KitService kitService;

    public KitController(KitService kitService) {
        this.kitService = kitService;
    }

    @PostMapping
    public ResponseEntity<KitResponse> insert(@RequestBody KitRequest request) {
        KitResponse saved = kitService.insert(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.id())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<KitResponse>> findAll() {
        return ResponseEntity.ok(kitService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KitResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(kitService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KitResponse> update(@PathVariable Long id, @RequestBody KitRequest request) {
        return ResponseEntity.ok(kitService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
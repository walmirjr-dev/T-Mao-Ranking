package com.walmir.tmaoranking.controller;

import com.walmir.tmaoranking.domain.Kit;
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
    public ResponseEntity<Kit> insert(@RequestBody Kit kit) {
        Kit saved = kitService.insert(kit);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Kit>> findAll() {
        return ResponseEntity.ok(kitService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kit> findById(@PathVariable Long id) {
        return ResponseEntity.ok(kitService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Kit> update(@PathVariable Long id, @RequestBody Kit kit) {
        return ResponseEntity.ok(kitService.update(id, kit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
package com.walmir.tmaoranking.service;

import com.walmir.tmaoranking.domain.Kit;
import com.walmir.tmaoranking.repository.KitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitService {

    private final KitRepository kitRepository;

    public KitService(KitRepository kitRepository) {
        this.kitRepository = kitRepository;
    }

    public Kit insert(Kit kit) {
        return kitRepository.save(kit);
    }

    public List<Kit> findAll() {
        return kitRepository.findAll();
    }

    public Kit findById(Long id) {
        return kitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kit not found: " + id));
    }
}
package com.walmir.tmaoranking.service;

import com.walmir.tmaoranking.domain.Kit;
import com.walmir.tmaoranking.domain.enums.KitType;
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
        validateDuplicate(kit);
        return kitRepository.save(kit);
    }

    public List<Kit> findAll() {
        return kitRepository.findAll();
    }

    public Kit findById(Long id) {
        return kitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kit not found: " + id));
    }

    public Kit update(Long id, Kit kit) {
        Kit existing = findById(id);
        existing.setReleaseYear(kit.getReleaseYear());
        existing.setName(kit.getName());
        existing.setKitType(kit.getKitType());
        existing.setImgUrl(kit.getImgUrl());
        existing.setDescription(kit.getDescription());
        return kitRepository.save(existing);
    }

    public void delete(Long id) {
        if (!kitRepository.existsById(id)) {
            throw new RuntimeException("Kit not found in Id: " + id);
        }

        kitRepository.deleteById(id);
    }

    private void validateDuplicate(Kit kit) {
        if (kit.getKitType() != KitType.SPECIAL && kitRepository.existsByKitTypeAndReleaseYear(kit.getKitType(), kit.getReleaseYear())) {
            throw new RuntimeException("A " + kit.getKitType() + " kit already exists for year " + kit.getReleaseYear());
        }
        if (kit.getKitType() == KitType.SPECIAL && kitRepository.existsByNameAndKitTypeAndReleaseYear(kit.getName(), kit.getKitType(), kit.getReleaseYear())) {
            throw new RuntimeException("This special kit already exists for year " + kit.getReleaseYear());
        }
    }
}
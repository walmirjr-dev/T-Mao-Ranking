package com.walmir.tmaoranking.service;

import com.walmir.tmaoranking.domain.Kit;
import com.walmir.tmaoranking.domain.enums.KitType;
import com.walmir.tmaoranking.dto.request.KitRequest;
import com.walmir.tmaoranking.dto.response.KitResponse;
import com.walmir.tmaoranking.repository.KitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KitService {

    private final KitRepository kitRepository;

    public KitService(KitRepository kitRepository) {
        this.kitRepository = kitRepository;
    }

    public KitResponse insert(KitRequest request) {
        Kit kit = new Kit();
        kit.setReleaseYear(request.releaseYear());
        kit.setName(request.name());
        kit.setKitType(request.kitType());
        kit.setImgUrl(request.imgUrl());
        kit.setDescription(request.description());
        validateDuplicate(kit);
        return KitResponse.from(kitRepository.save(kit));
    }

    public List<KitResponse> findAll() {
        return kitRepository.findAll().stream()
                .map(KitResponse::from)
                .toList();
    }

    public KitResponse findById(Long id) {
        return KitResponse.from(kitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kit not found: " + id)));
    }

    public KitResponse update(Long id, KitRequest request) {
        Kit existing = kitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kit not found: " + id));
        existing.setReleaseYear(request.releaseYear());
        existing.setName(request.name());
        existing.setKitType(request.kitType());
        existing.setImgUrl(request.imgUrl());
        existing.setDescription(request.description());
        return KitResponse.from(kitRepository.save(existing));
    }

    public void delete(Long id) {
        if (!kitRepository.existsById(id)) {
            throw new RuntimeException("Kit not found in Id: " + id);
        }

        kitRepository.deleteById(id);
    }

    public List<KitResponse> findByYear(Integer year) {
        return kitRepository.findByReleaseYear(year).stream()
                .map(KitResponse::from)
                .toList();
    }

    public List<KitResponse> findByYearBetween(Integer startYear, Integer endYear) {
        return kitRepository.findByReleaseYearBetween(startYear, endYear).stream()
                .map(KitResponse::from)
                .toList();
    }

    public List<KitResponse> findByKitType(KitType kitType) {
        return kitRepository.findByKitType(kitType).stream()
                .map(KitResponse::from)
                .toList();
    }

    public List<KitResponse> findByName(String name) {
        return kitRepository.findByNameContainingIgnoreCase(name).stream()
                .map(KitResponse::from)
                .toList();
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
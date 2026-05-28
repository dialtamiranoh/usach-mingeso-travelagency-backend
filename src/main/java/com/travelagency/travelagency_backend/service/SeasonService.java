// SeasonService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.SeasonEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor

public class SeasonService {

    private final SeasonRepository seasonRepository;

    public List<SeasonEntity> findAll() {
        return seasonRepository.findAll();
    }

    public Optional<SeasonEntity> findById(Long id) {
        return seasonRepository.findById(id);
    }

    public Optional<SeasonEntity> findByName(String name) {
        return seasonRepository.findByName(name);
    }

    public List<SeasonEntity> findByStatus(StatusEntity status) {
        return seasonRepository.findByStatus(status);
    }

    public SeasonEntity save(SeasonEntity season) {
        return seasonRepository.save(season);
    }

    public SeasonEntity update(SeasonEntity season) {
        return seasonRepository.save(season);
    }

    public void deleteById(Long id) {
        seasonRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return seasonRepository.existsByName(name);
    }
}
// DestinationService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.DestinationEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public List<DestinationEntity> findAll() {
        return destinationRepository.findAll();
    }

    public Optional<DestinationEntity> findById(Long id) {
        return destinationRepository.findById(id);
    }

    public Optional<DestinationEntity> findByName(String name) {
        return destinationRepository.findByName(name);
    }

    public List<DestinationEntity> findByStatus(StatusEntity status) {
        return destinationRepository.findByStatus(status);
    }

    public DestinationEntity save(DestinationEntity destination) {
        return destinationRepository.save(destination);
    }

    public DestinationEntity update(DestinationEntity destination) {
        return destinationRepository.save(destination);
    }

    public void deleteById(Long id) {
        destinationRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return destinationRepository.existsByName(name);
    }
}
// StatusService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;

    public List<StatusEntity> findAll() {
        return statusRepository.findAll();
    }

    public Optional<StatusEntity> findById(Long id) {
        return statusRepository.findById(id);
    }

    public List<StatusEntity> findByEntityType(String entityType) {
        return statusRepository.findByEntityType(entityType);
    }

    public StatusEntity save(StatusEntity status) {
        return statusRepository.save(status);
    }

    public StatusEntity update(StatusEntity status) {
        return statusRepository.save(status);
    }

    public void deleteById(Long id) {
        statusRepository.deleteById(id);
    }

}
// PackageServiceService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.PackageServiceEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.repository.PackageServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PackageServiceService {

    private final PackageServiceRepository packageServiceRepository;

    public List<PackageServiceEntity> findAll() {
        return packageServiceRepository.findAll();
    }

    public Optional<PackageServiceEntity> findById(Long id) {
        return packageServiceRepository.findById(id);
    }

    public List<PackageServiceEntity> findByStatus(StatusEntity status) {
        return packageServiceRepository.findByStatus(status);
    }

    public PackageServiceEntity save(PackageServiceEntity packageServiceEntity) {
        return packageServiceRepository.save(packageServiceEntity);
    }

    public PackageServiceEntity update(PackageServiceEntity packageServiceEntity) {
        return packageServiceRepository.save(packageServiceEntity);
    }

    public void deleteById(Long id) {
        packageServiceRepository.deleteById(id);
    }

}
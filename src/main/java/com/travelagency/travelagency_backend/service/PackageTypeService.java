// PackageTypeService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.PackageTypeEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.repository.PackageTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PackageTypeService {

    private final PackageTypeRepository packageTypeRepository;

    public List<PackageTypeEntity> findAll() {
        return packageTypeRepository.findAll();
    }

    public Optional<PackageTypeEntity> findById(Long id) {
        return packageTypeRepository.findById(id);
    }


    public List<PackageTypeEntity> findByStatus(StatusEntity status) {
        return packageTypeRepository.findByStatus(status);
    }

    public PackageTypeEntity save(PackageTypeEntity packageType) {
        return packageTypeRepository.save(packageType);
    }

    public PackageTypeEntity update(PackageTypeEntity packageType) {
        return packageTypeRepository.save(packageType);
    }

    public void deleteById(Long id) {
        packageTypeRepository.deleteById(id);
    }

}
// TouristPackageService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.TouristPackageEntity;
import com.travelagency.travelagency_backend.entity.CategoryEntity;
import com.travelagency.travelagency_backend.entity.DestinationEntity;
import com.travelagency.travelagency_backend.entity.PackageTypeEntity;
import com.travelagency.travelagency_backend.entity.SeasonEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.repository.TouristPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TouristPackageService {

    private final TouristPackageRepository touristPackageRepository;

    public List<TouristPackageEntity> findAll() {
        return touristPackageRepository.findAll();
    }

    public Optional<TouristPackageEntity> findById(Long id) {
        return touristPackageRepository.findById(id);
    }

    public List<TouristPackageEntity> findByStatus(StatusEntity status) {
        return touristPackageRepository.findByStatus(status);
    }

    public List<TouristPackageEntity> findByDestination(DestinationEntity destination) {
        return touristPackageRepository.findByDestination(destination);
    }

    public List<TouristPackageEntity> findByCategory(CategoryEntity category) {
        return touristPackageRepository.findByCategory(category);
    }

    public List<TouristPackageEntity> findBySeason(SeasonEntity season) {
        return touristPackageRepository.findBySeason(season);
    }

    public List<TouristPackageEntity> findByType(PackageTypeEntity type) {
        return touristPackageRepository.findByType(type);
    }

    public List<TouristPackageEntity> findAvailableWithFilters(
            DestinationEntity destination,
            CategoryEntity category,
            PackageTypeEntity type,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDate startDate,
            LocalDate endDate) {
        return touristPackageRepository.findAvailableWithFilters(
                destination, category, type, minPrice, maxPrice, startDate, endDate);
    }

    public TouristPackageEntity save(TouristPackageEntity touristPackage) {
        return touristPackageRepository.save(touristPackage);
    }

    public TouristPackageEntity update(TouristPackageEntity touristPackage) {
        return touristPackageRepository.save(touristPackage);
    }

    public void deleteById(Long id) {
        touristPackageRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return touristPackageRepository.existsById(id);
    }
}
// TouristPackageRepository.java
package com.travelagency.travelagency_backend.repository;

import com.travelagency.travelagency_backend.entity.TouristPackageEntity;
import com.travelagency.travelagency_backend.entity.CategoryEntity;
import com.travelagency.travelagency_backend.entity.DestinationEntity;
import com.travelagency.travelagency_backend.entity.PackageTypeEntity;
import com.travelagency.travelagency_backend.entity.SeasonEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TouristPackageRepository extends JpaRepository<TouristPackageEntity, Long> {
    List<TouristPackageEntity> findByStatus(StatusEntity status);
    List<TouristPackageEntity> findByDestination(DestinationEntity destination);
    List<TouristPackageEntity> findByCategory(CategoryEntity category);
    List<TouristPackageEntity> findBySeason(SeasonEntity season);
    List<TouristPackageEntity> findByType(PackageTypeEntity type);

    @Query("SELECT p FROM TouristPackage p WHERE p.status.name = 'AVAILABLE' " +
            "AND (:destination IS NULL OR p.destination = :destination) " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND (:type IS NULL OR p.type = :type) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:startDate IS NULL OR p.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR p.endDate <= :endDate)")
    List<TouristPackageEntity> findAvailableWithFilters(
            @Param("destination") DestinationEntity destination,
            @Param("category") CategoryEntity category,
            @Param("type") PackageTypeEntity type,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
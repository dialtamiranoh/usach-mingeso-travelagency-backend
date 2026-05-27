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

    @Query(value = "SELECT p.* FROM tourist_packages p " +
            "JOIN statuses s ON p.status_id = s.id " +
            "WHERE s.name = 'AVAILABLE' " +
            "AND (:destinationId IS NULL OR p.destination_id = :destinationId) " +
            "AND (:categoryId IS NULL OR p.category_id = :categoryId) " +
            "AND (:typeId IS NULL OR p.package_type_id = :typeId) " +
            "AND (:minPrice IS NULL OR p.price >= CAST(:minPrice AS NUMERIC)) " +
            "AND (:maxPrice IS NULL OR p.price <= CAST(:maxPrice AS NUMERIC)) " +
            "AND (:startDate IS NULL OR p.start_date >= CAST(:startDate AS DATE)) " +
            "AND (:endDate IS NULL OR p.end_date <= CAST(:endDate AS DATE))",
            nativeQuery = true)
    List<TouristPackageEntity> findAvailableWithFilters(
            @Param("destinationId") Long destinationId,
            @Param("categoryId") Long categoryId,
            @Param("typeId") Long typeId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT p FROM TouristPackageEntity p WHERE p.status.name = 'AVAILABLE'")
    List<TouristPackageEntity> findAvailablePackages();
}
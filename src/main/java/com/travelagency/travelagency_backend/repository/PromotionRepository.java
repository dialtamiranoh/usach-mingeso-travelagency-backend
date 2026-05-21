// PromotionRepository.java
package com.travelagency.travelagency_backend.repository;

import com.travelagency.travelagency_backend.entity.PromotionEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {
    List<PromotionEntity> findByStatus(StatusEntity status);

    @Query("SELECT p FROM PromotionEntity p WHERE p.status.name = 'ACTIVE' " +
            "AND (p.startDate IS NULL OR p.startDate <= :now) " +
            "AND (p.endDate IS NULL OR p.endDate >= :now)")
    List<PromotionEntity> findActivePromotions(@Param("now") LocalDateTime now);

    @Query("SELECT p FROM PromotionEntity p " +
            "JOIN p.touristPackages tp " +
            "WHERE tp.id = :packageId " +
            "AND p.status.name = 'ACTIVE' " +
            "AND (p.startDate IS NULL OR p.startDate <= :now) " +
            "AND (p.endDate IS NULL OR p.endDate >= :now)")
    List<PromotionEntity> findActivePromotionsByPackageId(
            @Param("packageId") Long packageId,
            @Param("now") LocalDateTime now
    );
}
// PackageTypeRepository.java
package com.travelagency.travelagency_backend.repository;

import com.travelagency.travelagency_backend.entity.PackageTypeEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PackageTypeRepository extends JpaRepository<PackageTypeEntity, Long> {
    Optional<PackageTypeEntity> findByName(String name);
    boolean existsByName(String name);
    List<PackageTypeEntity> findByStatus(StatusEntity status);
}
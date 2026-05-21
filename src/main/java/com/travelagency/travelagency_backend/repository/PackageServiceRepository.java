// PackageServiceRepository.java
package com.travelagency.travelagency_backend.repository;

import com.travelagency.travelagency_backend.entity.PackageServiceEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PackageServiceRepository extends JpaRepository<PackageServiceEntity, Long> {
    Optional<PackageServiceEntity> findByName(String name);
    boolean existsByName(String name);
    List<PackageServiceEntity> findByStatus(StatusEntity status);
}
// DestinationRepository.java
package com.travelagency.travelagency_backend.repository;

import com.travelagency.travelagency_backend.entity.DestinationEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DestinationRepository extends JpaRepository<DestinationEntity, Long> {
    Optional<DestinationEntity> findByName(String name);
    boolean existsByName(String name);
    List<DestinationEntity> findByStatus(StatusEntity status);
}
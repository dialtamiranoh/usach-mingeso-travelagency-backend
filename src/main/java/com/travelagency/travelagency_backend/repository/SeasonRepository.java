// SeasonRepository.java
package com.travelagency.travelagency_backend.repository;

import com.travelagency.travelagency_backend.entity.SeasonEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<SeasonEntity, Long> {
    Optional<SeasonEntity> findByName(String name);
    boolean existsByName(String name);
    List<SeasonEntity> findByStatus(StatusEntity status);
}
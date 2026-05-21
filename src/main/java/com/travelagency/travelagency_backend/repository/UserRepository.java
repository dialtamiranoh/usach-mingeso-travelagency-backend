// UserRepository.java
package com.travelagency.travelagency_backend.repository;

import com.travelagency.travelagency_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByKeycloakId(String keycloakId);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
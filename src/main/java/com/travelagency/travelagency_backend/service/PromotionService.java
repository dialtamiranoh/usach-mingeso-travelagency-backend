// PromotionService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.PromotionEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public List<PromotionEntity> findAll() {
        return promotionRepository.findAll();
    }

    public Optional<PromotionEntity> findById(Long id) {
        return promotionRepository.findById(id);
    }

    public List<PromotionEntity> findByStatus(StatusEntity status) {
        return promotionRepository.findByStatus(status);
    }

    public List<PromotionEntity> findActivePromotions() {
        return promotionRepository.findActivePromotions(LocalDateTime.now());
    }

    public List<PromotionEntity> findActivePromotionsByPackageId(Long packageId) {
        return promotionRepository.findActivePromotionsByPackageId(packageId, LocalDateTime.now());
    }

    public PromotionEntity save(PromotionEntity promotion) {
        return promotionRepository.save(promotion);
    }

    public PromotionEntity update(PromotionEntity promotion) {
        return promotionRepository.save(promotion);
    }

    public void deleteById(Long id) {
        promotionRepository.deleteById(id);
    }

}
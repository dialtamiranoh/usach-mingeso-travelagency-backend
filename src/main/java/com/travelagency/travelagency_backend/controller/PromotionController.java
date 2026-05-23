// PromotionController.java
package com.travelagency.travelagency_backend.controller;

import com.travelagency.travelagency_backend.entity.PromotionEntity;
import com.travelagency.travelagency_backend.service.PromotionService;
import com.travelagency.travelagency_backend.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;
    private final StatusService statusService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"", "/"})
    public ResponseEntity<List<PromotionEntity>> findAll() {
        return ResponseEntity.ok(promotionService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PromotionEntity> findById(@PathVariable Long id) {
        return promotionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active")
    public ResponseEntity<List<PromotionEntity>> findActivePromotions() {
        return ResponseEntity.ok(promotionService.findActivePromotions());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active/package/{packageId}")
    public ResponseEntity<List<PromotionEntity>> findActivePromotionsByPackageId(@PathVariable Long packageId) {
        return ResponseEntity.ok(promotionService.findActivePromotionsByPackageId(packageId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<PromotionEntity>> findByStatus(@PathVariable Long statusId) {
        return statusService.findById(statusId)
                .map(status -> ResponseEntity.ok(promotionService.findByStatus(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PromotionEntity> save(@RequestBody PromotionEntity promotion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionService.save(promotion));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PromotionEntity> update(@PathVariable Long id, @RequestBody PromotionEntity promotion) {
        if (promotionService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        promotion.setId(id);
        return ResponseEntity.ok(promotionService.update(promotion));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (promotionService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        promotionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
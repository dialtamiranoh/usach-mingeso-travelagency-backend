// TouristPackageController.java
package com.travelagency.travelagency_backend.controller;

import com.travelagency.travelagency_backend.entity.TouristPackageEntity;
import com.travelagency.travelagency_backend.entity.CategoryEntity;
import com.travelagency.travelagency_backend.entity.DestinationEntity;
import com.travelagency.travelagency_backend.entity.PackageTypeEntity;
import com.travelagency.travelagency_backend.entity.SeasonEntity;
import com.travelagency.travelagency_backend.service.TouristPackageService;
import com.travelagency.travelagency_backend.service.CategoryService;
import com.travelagency.travelagency_backend.service.DestinationService;
import com.travelagency.travelagency_backend.service.PackageTypeService;
import com.travelagency.travelagency_backend.service.SeasonService;
import com.travelagency.travelagency_backend.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tourist-packages")
@RequiredArgsConstructor
public class TouristPackageController {

    private final TouristPackageService touristPackageService;
    private final CategoryService categoryService;
    private final DestinationService destinationService;
    private final PackageTypeService packageTypeService;
    private final SeasonService seasonService;
    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<List<TouristPackageEntity>> findAll() {
        return ResponseEntity.ok(touristPackageService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TouristPackageEntity> findById(@PathVariable Long id) {
        return touristPackageService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<TouristPackageEntity>> findByStatus(@PathVariable Long statusId) {
        return statusService.findById(statusId)
                .map(status -> ResponseEntity.ok(touristPackageService.findByStatus(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/available")
    public ResponseEntity<List<TouristPackageEntity>> findAvailableWithFilters(
            @RequestParam(required = false) Long destinationId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        DestinationEntity destination = destinationId != null ?
                destinationService.findById(destinationId).orElse(null) : null;
        CategoryEntity category = categoryId != null ?
                categoryService.findById(categoryId).orElse(null) : null;
        PackageTypeEntity type = typeId != null ?
                packageTypeService.findById(typeId).orElse(null) : null;

        return ResponseEntity.ok(touristPackageService.findAvailableWithFilters(
                destination, category, type, minPrice, maxPrice, startDate, endDate));
    }

    @PostMapping
    public ResponseEntity<TouristPackageEntity> save(@RequestBody TouristPackageEntity touristPackage) {
        return ResponseEntity.status(HttpStatus.CREATED).body(touristPackageService.save(touristPackage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TouristPackageEntity> update(@PathVariable Long id, @RequestBody TouristPackageEntity touristPackage) {
        if (touristPackageService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        touristPackage.setId(id);
        return ResponseEntity.ok(touristPackageService.update(touristPackage));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (touristPackageService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        touristPackageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
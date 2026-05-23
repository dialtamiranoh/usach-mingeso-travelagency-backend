// PackageTypeController.java
package com.travelagency.travelagency_backend.controller;

import com.travelagency.travelagency_backend.entity.PackageTypeEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.service.PackageTypeService;
import com.travelagency.travelagency_backend.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/package-types")
@RequiredArgsConstructor
public class PackageTypeController {

    private final PackageTypeService packageTypeService;
    private final StatusService statusService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PackageTypeEntity>> findAll() {
        return ResponseEntity.ok(packageTypeService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PackageTypeEntity> findById(@PathVariable Long id) {
        return packageTypeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<PackageTypeEntity>> findByStatus(@PathVariable Long statusId) {
        return statusService.findById(statusId)
                .map(status -> ResponseEntity.ok(packageTypeService.findByStatus(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PackageTypeEntity> save(@RequestBody PackageTypeEntity packageType) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packageTypeService.save(packageType));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PackageTypeEntity> update(@PathVariable Long id, @RequestBody PackageTypeEntity packageType) {
        if (packageTypeService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        packageType.setId(id);
        return ResponseEntity.ok(packageTypeService.update(packageType));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (packageTypeService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        packageTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
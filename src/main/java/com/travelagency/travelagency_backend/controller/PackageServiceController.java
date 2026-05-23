// PackageServiceController.java
package com.travelagency.travelagency_backend.controller;

import com.travelagency.travelagency_backend.entity.PackageServiceEntity;
import com.travelagency.travelagency_backend.service.PackageServiceService;
import com.travelagency.travelagency_backend.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/package-services")
@RequiredArgsConstructor
public class PackageServiceController {

    private final PackageServiceService packageServiceService;
    private final StatusService statusService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PackageServiceEntity>> findAll() {
        return ResponseEntity.ok(packageServiceService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PackageServiceEntity> findById(@PathVariable Long id) {
        return packageServiceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<PackageServiceEntity>> findByStatus(@PathVariable Long statusId) {
        return statusService.findById(statusId)
                .map(status -> ResponseEntity.ok(packageServiceService.findByStatus(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PackageServiceEntity> save(@RequestBody PackageServiceEntity packageService) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packageServiceService.save(packageService));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PackageServiceEntity> update(@PathVariable Long id, @RequestBody PackageServiceEntity packageService) {
        if (packageServiceService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        packageService.setId(id);
        return ResponseEntity.ok(packageServiceService.update(packageService));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (packageServiceService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        packageServiceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
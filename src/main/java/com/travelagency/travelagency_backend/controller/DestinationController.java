// DestinationController.java
package com.travelagency.travelagency_backend.controller;

import com.travelagency.travelagency_backend.entity.DestinationEntity;
import com.travelagency.travelagency_backend.service.DestinationService;
import com.travelagency.travelagency_backend.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;
    private final StatusService statusService;

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<DestinationEntity>> findAll() {
        return ResponseEntity.ok(destinationService.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<DestinationEntity> findById(@PathVariable Long id) {
        return destinationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<DestinationEntity>> findByStatus(@PathVariable Long statusId) {
        return statusService.findById(statusId)
                .map(status -> ResponseEntity.ok(destinationService.findByStatus(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DestinationEntity> save(@RequestBody DestinationEntity destination) {
        return ResponseEntity.status(HttpStatus.CREATED).body(destinationService.save(destination));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DestinationEntity> update(@PathVariable Long id, @RequestBody DestinationEntity destination) {
        if (destinationService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        destination.setId(id);
        return ResponseEntity.ok(destinationService.update(destination));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (destinationService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        destinationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
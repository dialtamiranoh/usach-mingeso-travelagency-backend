// SeasonController.java
package com.travelagency.travelagency_backend.controller;

import com.travelagency.travelagency_backend.entity.SeasonEntity;
import com.travelagency.travelagency_backend.service.SeasonService;
import com.travelagency.travelagency_backend.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/seasons")
@RequiredArgsConstructor
public class SeasonController {

    private final SeasonService seasonService;
    private final StatusService statusService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SeasonEntity>> findAll() {
        return ResponseEntity.ok(seasonService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SeasonEntity> findById(@PathVariable Long id) {
        return seasonService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<SeasonEntity>> findByStatus(@PathVariable Long statusId) {
        return statusService.findById(statusId)
                .map(status -> ResponseEntity.ok(seasonService.findByStatus(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SeasonEntity> save(@RequestBody SeasonEntity season) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seasonService.save(season));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SeasonEntity> update(@PathVariable Long id, @RequestBody SeasonEntity season) {
        if (seasonService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        season.setId(id);
        return ResponseEntity.ok(seasonService.update(season));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (seasonService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        seasonService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
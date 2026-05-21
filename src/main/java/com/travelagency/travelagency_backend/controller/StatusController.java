// StatusController.java
package com.travelagency.travelagency_backend.controller;

import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping
    public ResponseEntity<List<StatusEntity>> findAll() {
        return ResponseEntity.ok(statusService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusEntity> findById(@PathVariable Long id) {
        return statusService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entity-type/{entityType}")
    public ResponseEntity<List<StatusEntity>> findByEntityType(@PathVariable String entityType) {
        return ResponseEntity.ok(statusService.findByEntityType(entityType));
    }

    @PostMapping
    public ResponseEntity<StatusEntity> save(@RequestBody StatusEntity status) {
        return ResponseEntity.status(HttpStatus.CREATED).body(statusService.save(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusEntity> update(@PathVariable Long id, @RequestBody StatusEntity status) {
        if (statusService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        status.setId(id);
        return ResponseEntity.ok(statusService.update(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (statusService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        statusService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
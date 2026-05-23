// UserController.java
package com.travelagency.travelagency_backend.controller;

import com.travelagency.travelagency_backend.entity.UserEntity;
import com.travelagency.travelagency_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserEntity>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> findById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/keycloak/{keycloakId}")
    public ResponseEntity<UserEntity> findByKeycloakId(@PathVariable String keycloakId) {
        return userService.findByKeycloakId(keycloakId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PostMapping
    public ResponseEntity<UserEntity> save(@RequestBody UserEntity user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> update(@PathVariable Long id, @RequestBody UserEntity user) {
        if (userService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        return ResponseEntity.ok(userService.update(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (userService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
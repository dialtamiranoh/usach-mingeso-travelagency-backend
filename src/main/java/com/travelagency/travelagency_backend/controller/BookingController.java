// BookingController.java
package com.travelagency.travelagency_backend.controller;

import com.travelagency.travelagency_backend.entity.BookingEntity;
import com.travelagency.travelagency_backend.service.BookingService;
import com.travelagency.travelagency_backend.service.StatusService;
import com.travelagency.travelagency_backend.service.TouristPackageService;
import com.travelagency.travelagency_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;
    private final TouristPackageService touristPackageService;
    private final StatusService statusService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"", "/"})
    public ResponseEntity<List<BookingEntity>> findAll() {
        return ResponseEntity.ok(bookingService.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/{id}")
    public ResponseEntity<BookingEntity> findById(@PathVariable Long id) {
        return bookingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingEntity>> findByUser(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(user -> ResponseEntity.ok(bookingService.findByUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping("/keycloak/{keycloakId}")
    public ResponseEntity<List<BookingEntity>> findByKeycloakId(@PathVariable String keycloakId) {
        return userService.findByKeycloakId(keycloakId)
                .map(user -> ResponseEntity.ok(bookingService.findByUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/package/{packageId}")
    public ResponseEntity<List<BookingEntity>> findByTouristPackage(@PathVariable Long packageId) {
        return touristPackageService.findById(packageId)
                .map(pkg -> ResponseEntity.ok(bookingService.findByTouristPackage(pkg)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<BookingEntity>> findByStatus(@PathVariable Long statusId) {
        return statusService.findById(statusId)
                .map(status -> ResponseEntity.ok(bookingService.findByStatus(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/expired")
    public ResponseEntity<List<BookingEntity>> findExpiredBookings() {
        return ResponseEntity.ok(bookingService.findExpiredBookings());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/date-range")
    public ResponseEntity<List<BookingEntity>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(bookingService.findByDateRangeExcludingCancelled(startDate, endDate));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(
            @RequestParam Long packageId,
            @RequestParam int passengerCount,
            Authentication authentication) {
        try {
            String keycloakId = authentication.getName();
            BookingEntity booking = bookingService.createBooking(packageId, keycloakId, passengerCount);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping({"", "/"})
    public ResponseEntity<BookingEntity> save(@RequestBody BookingEntity booking) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.update(booking));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookingEntity> update(@PathVariable Long id, @RequestBody BookingEntity booking) {
        if (bookingService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        booking.setId(id);
        return ResponseEntity.ok(bookingService.update(booking));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (bookingService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bookingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
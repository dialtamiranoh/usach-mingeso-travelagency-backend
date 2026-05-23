// PaymentController.java
package com.travelagency.travelagency_backend.controller;

import com.travelagency.travelagency_backend.entity.PaymentEntity;
import com.travelagency.travelagency_backend.service.BookingService;
import com.travelagency.travelagency_backend.service.PaymentService;
import com.travelagency.travelagency_backend.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final BookingService bookingService;
    private final StatusService statusService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PaymentEntity>> findAll() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentEntity> findById(@PathVariable Long id) {
        return paymentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentEntity> findByBooking(@PathVariable Long bookingId) {
        return bookingService.findById(bookingId)
                .map(booking -> paymentService.findByBooking(booking)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/transaction/{transactionCode}")
    public ResponseEntity<PaymentEntity> findByTransactionCode(@PathVariable String transactionCode) {
        return paymentService.findByTransactionCode(transactionCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<PaymentEntity>> findByStatus(@PathVariable Long statusId) {
        return statusService.findById(statusId)
                .map(status -> ResponseEntity.ok(paymentService.findByStatus(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentEntity>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.findByDateRange(startDate, endDate));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PaymentEntity> save(@RequestBody PaymentEntity payment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.save(payment));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentEntity> update(@PathVariable Long id, @RequestBody PaymentEntity payment) {
        if (paymentService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        payment.setId(id);
        return ResponseEntity.ok(paymentService.update(payment));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (paymentService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        paymentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
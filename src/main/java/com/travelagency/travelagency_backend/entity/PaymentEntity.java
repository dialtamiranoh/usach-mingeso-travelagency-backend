// PaymentEntity.java
package com.travelagency.travelagency_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private BookingEntity booking;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "card_expiry", nullable = false)
    private String cardExpiry;

    @Column(name = "card_cvv", nullable = false)
    private String cardCvv;

    @Column(name = "transaction_code", nullable = false, unique = true)
    private String transactionCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusEntity status;

    @Column(name = "paid_at", nullable = false, updatable = false)
    private LocalDateTime paidAt;

    @PrePersist
    protected void onCreate() {
        this.paidAt = LocalDateTime.now();
    }
}
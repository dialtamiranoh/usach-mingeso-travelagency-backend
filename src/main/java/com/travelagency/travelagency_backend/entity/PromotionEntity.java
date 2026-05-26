// PromotionEntity.java
package com.travelagency.travelagency_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "min_passengers")
    private Integer minPassengers;

    @Column(name = "min_bookings_session")
    private Integer minBookingsSession;

    @Column(name = "min_bookings_history")
    private Integer minBookingsHistory;

    @Column(name = "is_accumulable", nullable = false)
    private Boolean isAccumulable;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusEntity status;

    @JsonIgnore
    @ManyToMany(mappedBy = "promotions", fetch = FetchType.LAZY)
    private List<TouristPackageEntity> touristPackages;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
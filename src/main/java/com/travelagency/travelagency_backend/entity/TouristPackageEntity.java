// TouristPackageEntity.java
package com.travelagency.travelagency_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tourist_packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TouristPackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_id", nullable = false)
    private DestinationEntity destination;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "duration_days", nullable = false)
    private int durationDays;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "total_slots", nullable = false)
    private int totalSlots;

    @Column(name = "available_slots", nullable = false)
    private int availableSlots;

    @Column(columnDefinition = "TEXT")
    private String conditions;

    @Column(columnDefinition = "TEXT")
    private String restrictions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_type_id", nullable = false)
    private PackageTypeEntity type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "season_id", nullable = false)
    private SeasonEntity season;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusEntity status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tourist_package_services",
            joinColumns = @JoinColumn(name = "tourist_package_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<PackageServiceEntity> services;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tourist_package_promotions",
            joinColumns = @JoinColumn(name = "tourist_package_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    private List<PromotionEntity> promotions;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
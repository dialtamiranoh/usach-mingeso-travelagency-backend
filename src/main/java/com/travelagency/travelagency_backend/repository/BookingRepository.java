// BookingRepository.java
package com.travelagency.travelagency_backend.repository;

import com.travelagency.travelagency_backend.entity.BookingEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.entity.TouristPackageEntity;
import com.travelagency.travelagency_backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByUser(UserEntity user);
    List<BookingEntity> findByTouristPackage(TouristPackageEntity touristPackage);
    List<BookingEntity> findByStatus(StatusEntity status);
    List<BookingEntity> findByUserAndStatus(UserEntity user, StatusEntity status);

    @Query("SELECT COUNT(b) FROM BookingEntity b WHERE b.user = :user " +
            "AND b.status.name = 'CONFIRMED'")
    long countConfirmedBookingsByUser(@Param("user") UserEntity user);

    @Query("SELECT b FROM BookingEntity b WHERE b.status.name = 'PENDING_PAYMENT' " +
            "AND b.expiresAt <= :now")
    List<BookingEntity> findExpiredBookings(@Param("now") LocalDateTime now);

    @Query("SELECT b FROM BookingEntity b WHERE b.createdAt BETWEEN :startDate AND :endDate " +
            "AND b.status.name != 'CANCELLED'")
    List<BookingEntity> findByDateRangeExcludingCancelled(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
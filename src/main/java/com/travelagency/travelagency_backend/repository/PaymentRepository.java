// PaymentRepository.java
package com.travelagency.travelagency_backend.repository;

import com.travelagency.travelagency_backend.entity.PaymentEntity;
import com.travelagency.travelagency_backend.entity.BookingEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByBooking(BookingEntity booking);
    Optional<PaymentEntity> findByTransactionCode(String transactionCode);
    boolean existsByBooking(BookingEntity booking);
    List<PaymentEntity> findByStatus(StatusEntity status);

    @Query("SELECT p FROM PaymentEntity p WHERE p.paidAt BETWEEN :startDate AND :endDate")
    List<PaymentEntity> findByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
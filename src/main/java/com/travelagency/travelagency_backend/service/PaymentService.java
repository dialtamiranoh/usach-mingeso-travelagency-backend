// PaymentService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.BookingEntity;
import com.travelagency.travelagency_backend.entity.PaymentEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<PaymentEntity> findAll() {
        return paymentRepository.findAll();
    }

    public Optional<PaymentEntity> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public Optional<PaymentEntity> findByBooking(BookingEntity booking) {
        return paymentRepository.findByBooking(booking);
    }

    public Optional<PaymentEntity> findByTransactionCode(String transactionCode) {
        return paymentRepository.findByTransactionCode(transactionCode);
    }

    public boolean existsByBooking(BookingEntity booking) {
        return paymentRepository.existsByBooking(booking);
    }

    public List<PaymentEntity> findByStatus(StatusEntity status) {
        return paymentRepository.findByStatus(status);
    }

    public List<PaymentEntity> findByDateRange(
            LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByDateRange(startDate, endDate);
    }

    public PaymentEntity save(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    public PaymentEntity update(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return paymentRepository.existsById(id);
    }
}
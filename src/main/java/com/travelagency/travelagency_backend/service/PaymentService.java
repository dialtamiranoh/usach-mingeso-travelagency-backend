package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.*;
import com.travelagency.travelagency_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final StatusRepository statusRepository;

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

    public List<PaymentEntity> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findByDateRange(startDate, endDate);
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

    @Transactional
    public PaymentEntity processPayment(Long bookingId, String cardNumber, String cardExpiry, String cardCvv) {

        // 1. Obtener reserva
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // 2. Validar estado de la reserva
        if (!booking.getStatus().getName().equals("PENDING_PAYMENT")) {
            throw new RuntimeException("La reserva no está en estado PENDING_PAYMENT");
        }

        // 3. Validar que no tenga pago previo
        if (paymentRepository.existsByBooking(booking)) {
            throw new RuntimeException("Esta reserva ya tiene un pago registrado");
        }

        // 4. Validar que no esté expirada
        if (booking.getExpiresAt() != null && booking.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("La reserva ha expirado");
        }

        // 5. Obtener estado APPROVED para el pago
        StatusEntity approvedStatus = statusRepository
                .findByNameAndEntityType("APPROVED", "PAYMENT")
                .orElseThrow(() -> new RuntimeException("Estado APPROVED no encontrado"));

        // 6. Obtener estado CONFIRMED para la reserva
        StatusEntity confirmedStatus = statusRepository
                .findByNameAndEntityType("CONFIRMED", "BOOKING")
                .orElseThrow(() -> new RuntimeException("Estado CONFIRMED no encontrado"));

        // 7. Crear pago
        PaymentEntity payment = new PaymentEntity();
        payment.setBooking(booking);
        payment.setAmount(booking.getFinalAmount());
        payment.setCardNumber(cardNumber);
        payment.setCardExpiry(cardExpiry);
        payment.setCardCvv(cardCvv);
        payment.setTransactionCode(UUID.randomUUID().toString());
        payment.setStatus(approvedStatus);

        paymentRepository.save(payment);

        // 8. Actualizar estado de la reserva a CONFIRMED
        booking.setStatus(confirmedStatus);
        bookingRepository.save(booking);

        return payment;
    }
}
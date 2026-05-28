// src/test/java/com/travelagency/travelagency_backend/service/PaymentServiceTest.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.*;
import com.travelagency.travelagency_backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private StatusRepository statusRepository;

    @InjectMocks
    private PaymentService paymentService;

    private BookingEntity booking;
    private StatusEntity pendingStatus;
    private StatusEntity confirmedStatus;
    private StatusEntity approvedStatus;

    @BeforeEach
    void setUp() {
        pendingStatus = new StatusEntity();
        pendingStatus.setId(1L);
        pendingStatus.setName("PENDING_PAYMENT");

        confirmedStatus = new StatusEntity();
        confirmedStatus.setId(2L);
        confirmedStatus.setName("CONFIRMED");

        approvedStatus = new StatusEntity();
        approvedStatus.setId(3L);
        approvedStatus.setName("APPROVED");

        booking = new BookingEntity();
        booking.setId(1L);
        booking.setStatus(pendingStatus);
        booking.setFinalAmount(new BigDecimal("200.00"));
        booking.setExpiresAt(LocalDateTime.now().plusHours(1));
    }

    @Test
    void processPayment_success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.existsByBooking(booking)).thenReturn(false);
        when(statusRepository.findByNameAndEntityType("APPROVED", "PAYMENT")).thenReturn(Optional.of(approvedStatus));
        when(statusRepository.findByNameAndEntityType("CONFIRMED", "BOOKING")).thenReturn(Optional.of(confirmedStatus));
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PaymentEntity result = paymentService.processPayment(1L, "1234567890123456", "12/26", "123");

        assertNotNull(result);
        assertNotNull(result.getTransactionCode());
        assertEquals(new BigDecimal("200.00"), result.getAmount());
        assertEquals("APPROVED", result.getStatus().getName());
        assertEquals("CONFIRMED", booking.getStatus().getName());
        verify(paymentRepository).save(any(PaymentEntity.class));
        verify(bookingRepository).save(booking);
    }

    @Test
    void processPayment_bookingNotFound_throwsException() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                paymentService.processPayment(99L, "1234", "12/26", "123"));
    }

    @Test
    void processPayment_bookingNotPending_throwsException() {
        booking.setStatus(confirmedStatus);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(RuntimeException.class, () ->
                paymentService.processPayment(1L, "1234", "12/26", "123"));
    }

    @Test
    void processPayment_alreadyPaid_throwsException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.existsByBooking(booking)).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                paymentService.processPayment(1L, "1234", "12/26", "123"));
    }

    @Test
    void processPayment_expiredBooking_throwsException() {
        booking.setExpiresAt(LocalDateTime.now().minusHours(1));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.existsByBooking(booking)).thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                paymentService.processPayment(1L, "1234", "12/26", "123"));
    }

    @Test
    void processPayment_generatesUniqueTransactionCode() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.existsByBooking(booking)).thenReturn(false);
        when(statusRepository.findByNameAndEntityType("APPROVED", "PAYMENT")).thenReturn(Optional.of(approvedStatus));
        when(statusRepository.findByNameAndEntityType("CONFIRMED", "BOOKING")).thenReturn(Optional.of(confirmedStatus));
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        PaymentEntity result = paymentService.processPayment(1L, "1234", "12/26", "123");

        assertNotNull(result.getTransactionCode());
        assertFalse(result.getTransactionCode().isEmpty());
    }
}
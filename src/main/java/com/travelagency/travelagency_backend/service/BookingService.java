// BookingService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.BookingEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.entity.TouristPackageEntity;
import com.travelagency.travelagency_backend.entity.UserEntity;
import com.travelagency.travelagency_backend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public List<BookingEntity> findAll() {
        return bookingRepository.findAll();
    }

    public Optional<BookingEntity> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<BookingEntity> findByUser(UserEntity user) {
        return bookingRepository.findByUser(user);
    }

    public List<BookingEntity> findByTouristPackage(TouristPackageEntity touristPackage) {
        return bookingRepository.findByTouristPackage(touristPackage);
    }

    public List<BookingEntity> findByStatus(StatusEntity status) {
        return bookingRepository.findByStatus(status);
    }

    public List<BookingEntity> findByUserAndStatus(UserEntity user, StatusEntity status) {
        return bookingRepository.findByUserAndStatus(user, status);
    }

    public long countConfirmedBookingsByUser(UserEntity user) {
        return bookingRepository.countConfirmedBookingsByUser(user);
    }

    public List<BookingEntity> findExpiredBookings() {
        return bookingRepository.findExpiredBookings(LocalDateTime.now());
    }

    public List<BookingEntity> findByDateRangeExcludingCancelled(
            LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findByDateRangeExcludingCancelled(startDate, endDate);
    }

    public BookingEntity save(BookingEntity booking) {
        return bookingRepository.save(booking);
    }

    public BookingEntity update(BookingEntity booking) {
        return bookingRepository.save(booking);
    }

    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return bookingRepository.existsById(id);
    }
}
package com.travelagency.travelagency_backend.config;

import com.travelagency.travelagency_backend.entity.BookingEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.entity.TouristPackageEntity;
import com.travelagency.travelagency_backend.repository.BookingRepository;
import com.travelagency.travelagency_backend.repository.StatusRepository;
import com.travelagency.travelagency_backend.repository.TouristPackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingExpirationJob {

    private final BookingRepository bookingRepository;
    private final StatusRepository statusRepository;
    private final TouristPackageRepository touristPackageRepository;

    @Scheduled(fixedRate = 60000) // cada 60 segundos
    @Transactional
    public void expireBookings() {
        List<BookingEntity> expired = bookingRepository.findExpiredBookings(LocalDateTime.now());

        if (expired.isEmpty()) return;

        StatusEntity expiredStatus = statusRepository
                .findByNameAndEntityType("EXPIRED", "BOOKING")
                .orElse(null);

        StatusEntity availableStatus = statusRepository
                .findByNameAndEntityType("AVAILABLE", "PACKAGE")
                .orElse(null);

        if (expiredStatus == null || availableStatus == null) {
            log.warn("Estados EXPIRED o AVAILABLE no encontrados");
            return;
        }

        for (BookingEntity booking : expired) {
            TouristPackageEntity pkg = booking.getTouristPackage();
            pkg.setAvailableSlots(pkg.getAvailableSlots() + booking.getPassengerCount());

            if (pkg.getStatus().getName().equals("SOLD_OUT")) {
                pkg.setStatus(availableStatus);
            }
            touristPackageRepository.save(pkg);

            booking.setStatus(expiredStatus);
            bookingRepository.save(booking);
            log.info("Reserva #{} expirada, cupos liberados", booking.getId());
        }
    }
}
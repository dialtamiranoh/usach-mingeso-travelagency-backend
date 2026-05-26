// BookingService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.*;
import com.travelagency.travelagency_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TouristPackageRepository touristPackageRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final PromotionRepository promotionRepository;

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

    public BookingEntity update(BookingEntity booking) {
        return bookingRepository.save(booking);
    }


    public boolean existsById(Long id) {
        return bookingRepository.existsById(id);
    }

    @Transactional
    public BookingEntity createBooking(Long packageId, String keycloakId, int passengerCount) {

        // 1. Obtener usuario
        UserEntity user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Obtener paquete
        TouristPackageEntity pkg = touristPackageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        // 3. Validar estado del paquete
        if (!pkg.getStatus().getName().equals("AVAILABLE")) {
            throw new RuntimeException("El paquete no está disponible");
        }

        // 4. Validar cupos
        if (passengerCount <= 0) {
            throw new RuntimeException("La cantidad de pasajeros debe ser mayor que cero");
        }
        if (pkg.getAvailableSlots() < passengerCount) {
            throw new RuntimeException("No hay suficientes cupos disponibles");
        }

        // 5. Calcular monto base
        BigDecimal baseAmount = pkg.getPrice().multiply(BigDecimal.valueOf(passengerCount));

        // 6. Aplicar descuentos de promociones activas
        List<PromotionEntity> activePromotions = promotionRepository
                .findActivePromotionsByPackageId(packageId, LocalDateTime.now());

        long confirmedBookings = bookingRepository.countConfirmedBookingsByUser(user);

        BigDecimal totalDiscountPct = BigDecimal.ZERO;
        List<String> discountDetails = new ArrayList<>();
        boolean hasAccumulable = false;
        boolean hasNonAccumulable = false;
        BigDecimal maxDiscountPct = new BigDecimal("20"); // límite máximo 20%

        for (PromotionEntity promo : activePromotions) {
            boolean applies = false;
            String reason = "";

            // Descuento por grupo
            if (promo.getMinPassengers() != null && passengerCount >= promo.getMinPassengers()) {
                applies = true;
                reason = "Descuento por grupo";
            }

            // Descuento cliente frecuente
            if (promo.getMinBookingsHistory() != null && confirmedBookings >= promo.getMinBookingsHistory()) {
                applies = true;
                reason = "Cliente frecuente";
            }

            // Descuento multi-paquete sesión
            if (promo.getMinBookingsSession() != null) {
                long sessionBookings = bookingRepository.findByUser(user).stream()
                        .filter(b -> b.getCreatedAt().isAfter(LocalDateTime.now().minusHours(1)))
                        .count();
                if (sessionBookings >= promo.getMinBookingsSession()) {
                    applies = true;
                    reason = "Compra múltiple";
                }
            }

            if (applies) {
                if (promo.getIsAccumulable()) {
                    hasAccumulable = true;
                    totalDiscountPct = totalDiscountPct.add(promo.getDiscountPercentage());
                    discountDetails.add(reason + ": " + promo.getDiscountPercentage() + "%");
                } else {
                    if (!hasNonAccumulable) {
                        hasNonAccumulable = true;
                        totalDiscountPct = totalDiscountPct.add(promo.getDiscountPercentage());
                        discountDetails.add(reason + ": " + promo.getDiscountPercentage() + "%");
                    }
                }
            }
        }

        // 7. Aplicar límite máximo de descuento
        if (totalDiscountPct.compareTo(maxDiscountPct) > 0) {
            totalDiscountPct = maxDiscountPct;
        }

        // 8. Calcular monto final
        BigDecimal discountAmount = baseAmount
                .multiply(totalDiscountPct)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal finalAmount = baseAmount.subtract(discountAmount);
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }

        // 9. Descontar cupos
        pkg.setAvailableSlots(pkg.getAvailableSlots() - passengerCount);
        if (pkg.getAvailableSlots() == 0) {
            StatusEntity soldOut = statusRepository
                    .findByNameAndEntityType("SOLD_OUT", "PACKAGE")
                    .orElseThrow(() -> new RuntimeException("Estado SOLD_OUT no encontrado"));
            pkg.setStatus(soldOut);
        }
        touristPackageRepository.save(pkg);

        // 10. Obtener estado PENDING_PAYMENT
        StatusEntity pendingStatus = statusRepository
                .findByNameAndEntityType("PENDING_PAYMENT", "BOOKING")
                .orElseThrow(() -> new RuntimeException("Estado PENDING_PAYMENT no encontrado"));

        // 11. Crear booking
        BookingEntity booking = new BookingEntity();
        booking.setUser(user);
        booking.setTouristPackage(pkg);
        booking.setPassengerCount(passengerCount);
        booking.setBaseAmount(baseAmount);
        booking.setDiscountAmount(discountAmount);
        booking.setFinalAmount(finalAmount);
        booking.setDiscountDetail(String.join(", ", discountDetails));
        booking.setStatus(pendingStatus);
        booking.setExpiresAt(LocalDateTime.now().plusSeconds(60));

        return bookingRepository.save(booking);
    }

    @Transactional
    public BookingEntity updateBooking(Long bookingId, StatusEntity newStatus, int newPassengerCount) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        String currentStatus = booking.getStatus().getName();

        // Estados finales
        if (currentStatus.equals("CANCELLED") || currentStatus.equals("EXPIRED")) {
            throw new RuntimeException("No se puede modificar una reserva en estado " + currentStatus);
        }

        // EXPIRED solo lo asigna el sistema
        if (newStatus.getName().equals("EXPIRED")) {
            throw new RuntimeException("El estado EXPIRED solo lo asigna el sistema");
        }

        TouristPackageEntity pkg = booking.getTouristPackage();
        int diff = newPassengerCount - booking.getPassengerCount();

        // Validar cupos si aumentan pasajeros
        if (diff > 0 && pkg.getAvailableSlots() < diff) {
            throw new RuntimeException("No hay suficientes cupos disponibles");
        }

        // Si se cancela, liberar todos los cupos
        if (newStatus.getName().equals("CANCELLED")) {
            pkg.setAvailableSlots(pkg.getAvailableSlots() + booking.getPassengerCount());
        } else {
            pkg.setAvailableSlots(pkg.getAvailableSlots() - diff);
        }

        // Actualizar estado del paquete
        if (pkg.getAvailableSlots() == 0) {
            StatusEntity soldOut = statusRepository
                    .findByNameAndEntityType("SOLD_OUT", "PACKAGE")
                    .orElseThrow(() -> new RuntimeException("Estado SOLD_OUT no encontrado"));
            pkg.setStatus(soldOut);
        } else if (pkg.getStatus().getName().equals("SOLD_OUT") && pkg.getAvailableSlots() > 0) {
            StatusEntity available = statusRepository
                    .findByNameAndEntityType("AVAILABLE", "PACKAGE")
                    .orElseThrow(() -> new RuntimeException("Estado AVAILABLE no encontrado"));
            pkg.setStatus(available);
        }
        touristPackageRepository.save(pkg);

        booking.setPassengerCount(newPassengerCount);
        booking.setStatus(newStatus);
        booking.setBaseAmount(pkg.getPrice().multiply(BigDecimal.valueOf(newPassengerCount)));
        booking.setFinalAmount(booking.getBaseAmount().subtract(booking.getDiscountAmount()));

        return bookingRepository.save(booking);
    }
}
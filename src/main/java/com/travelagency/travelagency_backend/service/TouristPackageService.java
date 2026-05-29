// TouristPackageService.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.TouristPackageEntity;
import com.travelagency.travelagency_backend.entity.CategoryEntity;
import com.travelagency.travelagency_backend.entity.DestinationEntity;
import com.travelagency.travelagency_backend.entity.PackageTypeEntity;
import com.travelagency.travelagency_backend.entity.SeasonEntity;
import com.travelagency.travelagency_backend.entity.StatusEntity;
import com.travelagency.travelagency_backend.repository.TouristPackageRepository;
import com.travelagency.travelagency_backend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TouristPackageService {

    private final TouristPackageRepository touristPackageRepository;

    public List<TouristPackageEntity> findAll() {
        return touristPackageRepository.findAll();
    }

    public Optional<TouristPackageEntity> findById(Long id) {
        return touristPackageRepository.findById(id);
    }

    public List<TouristPackageEntity> findByStatus(StatusEntity status) {
        return touristPackageRepository.findByStatus(status);
    }


    public List<TouristPackageEntity> findAvailableWithFilters(
            DestinationEntity destination,
            CategoryEntity category,
            PackageTypeEntity type,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDate startDate,
            LocalDate endDate) {

        List<TouristPackageEntity> all = touristPackageRepository.findAvailablePackages();

        return all.stream()
                .filter(p -> destination == null || p.getDestination().getId().equals(destination.getId()))
                .filter(p -> category == null || p.getCategory().getId().equals(category.getId()))
                .filter(p -> type == null || p.getType().getId().equals(type.getId()))
                .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                .filter(p -> startDate == null || !p.getStartDate().isBefore(startDate))
                .filter(p -> endDate == null || !p.getEndDate().isAfter(endDate))
                .collect(java.util.stream.Collectors.toList());
    }

    public TouristPackageEntity save(TouristPackageEntity touristPackage) {
        validatePackage(touristPackage);
        return touristPackageRepository.save(touristPackage);
    }

    public TouristPackageEntity update(TouristPackageEntity touristPackage) {
        validatePackage(touristPackage);

        TouristPackageEntity existing = touristPackageRepository.findById(touristPackage.getId())
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        boolean hasActiveBookings = bookingRepository.findByTouristPackage(existing).stream()
                .anyMatch(b -> b.getStatus().getName().equals("PENDING_PAYMENT")
                        || b.getStatus().getName().equals("CONFIRMED"));

        if (hasActiveBookings) {
            // Validar que no cambien fechas
            if (!existing.getStartDate().equals(touristPackage.getStartDate()) ||
                    !existing.getEndDate().equals(touristPackage.getEndDate())) {
                throw new RuntimeException("No se pueden modificar las fechas de un paquete con reservas activas");
            }
            // Validar que cupos totales no sean menores a los ya reservados
            long reservedSlots = existing.getTotalSlots() - existing.getAvailableSlots();
            if (touristPackage.getTotalSlots() < reservedSlots) {
                throw new RuntimeException("Los cupos totales no pueden ser menores a los cupos ya reservados (" + reservedSlots + ")");
            }
        }

        return touristPackageRepository.save(touristPackage);
    }

    private final BookingRepository bookingRepository;

    public void deleteById(Long id) {
        TouristPackageEntity pkg = touristPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        boolean hasActiveBookings = bookingRepository.findByTouristPackage(pkg).stream()
                .anyMatch(b -> b.getStatus().getName().equals("PENDING_PAYMENT")
                        || b.getStatus().getName().equals("CONFIRMED"));

        if (hasActiveBookings) {
            throw new RuntimeException("No se puede eliminar un paquete con reservas activas");
        }
        touristPackageRepository.deleteById(id);
    }

    private void validatePackage(TouristPackageEntity pkg) {
        if (pkg.getPrice() == null || pkg.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio del paquete debe ser mayor que cero");
        }
        if (pkg.getTotalSlots() <= 0) {
            throw new RuntimeException("Los cupos totales deben ser mayores que cero");
        }
        if (pkg.getStartDate() != null && pkg.getEndDate() != null
                && !pkg.getEndDate().isAfter(pkg.getStartDate())) {
            throw new RuntimeException("La fecha de término debe ser posterior a la fecha de inicio");
        }
    }

}
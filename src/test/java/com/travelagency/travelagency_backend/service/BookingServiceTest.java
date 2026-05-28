// src/test/java/com/travelagency/travelagency_backend/service/BookingServiceTest.java
package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.entity.*;
import com.travelagency.travelagency_backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private TouristPackageRepository touristPackageRepository;
    @Mock private UserRepository userRepository;
    @Mock private StatusRepository statusRepository;
    @Mock private PromotionRepository promotionRepository;

    @InjectMocks
    private BookingService bookingService;

    private UserEntity user;
    private TouristPackageEntity pkg;
    private StatusEntity availableStatus;
    private StatusEntity pendingStatus;
    private StatusEntity confirmedStatus;
    private StatusEntity cancelledStatus;
    private StatusEntity expiredStatus;
    private StatusEntity soldOutStatus;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setKeycloakId("test-keycloak-id");
        user.setFullName("Test User");
        user.setEmail("test@test.com");

        availableStatus = new StatusEntity();
        availableStatus.setId(1L);
        availableStatus.setName("AVAILABLE");
        availableStatus.setEntityType("PACKAGE");

        soldOutStatus = new StatusEntity();
        soldOutStatus.setId(2L);
        soldOutStatus.setName("SOLD_OUT");
        soldOutStatus.setEntityType("PACKAGE");

        pendingStatus = new StatusEntity();
        pendingStatus.setId(3L);
        pendingStatus.setName("PENDING_PAYMENT");
        pendingStatus.setEntityType("BOOKING");

        confirmedStatus = new StatusEntity();
        confirmedStatus.setId(4L);
        confirmedStatus.setName("CONFIRMED");
        confirmedStatus.setEntityType("BOOKING");

        cancelledStatus = new StatusEntity();
        cancelledStatus.setId(5L);
        cancelledStatus.setName("CANCELLED");
        cancelledStatus.setEntityType("BOOKING");

        expiredStatus = new StatusEntity();
        expiredStatus.setId(6L);
        expiredStatus.setName("EXPIRED");
        expiredStatus.setEntityType("BOOKING");

        pkg = new TouristPackageEntity();
        pkg.setId(1L);
        pkg.setName("Test Package");
        pkg.setPrice(new BigDecimal("100.00"));
        pkg.setTotalSlots(10);
        pkg.setAvailableSlots(10);
        pkg.setStatus(availableStatus);
        pkg.setStartDate(LocalDate.now().plusDays(10));
        pkg.setEndDate(LocalDate.now().plusDays(15));
        pkg.setDurationDays(5);
    }

    // ===== createBooking =====

    @Test
    void createBooking_success() {
        when(userRepository.findByKeycloakId("test-keycloak-id")).thenReturn(Optional.of(user));
        when(touristPackageRepository.findById(1L)).thenReturn(Optional.of(pkg));
        when(promotionRepository.findActivePromotionsByPackageId(any(), any())).thenReturn(new ArrayList<>());
        when(bookingRepository.countConfirmedBookingsByUser(user)).thenReturn(0L);
        when(statusRepository.findByNameAndEntityType("PENDING_PAYMENT", "BOOKING")).thenReturn(Optional.of(pendingStatus));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(touristPackageRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BookingEntity result = bookingService.createBooking(1L, "test-keycloak-id", 2);

        assertNotNull(result);
        assertEquals(2, result.getPassengerCount());
        assertEquals(new BigDecimal("200.00"), result.getBaseAmount());
        assertEquals(pendingStatus, result.getStatus());
        verify(touristPackageRepository).save(pkg);
        verify(bookingRepository).save(any(BookingEntity.class));
    }

    @Test
    void createBooking_userNotFound_throwsException() {
        when(userRepository.findByKeycloakId("invalid-id")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                bookingService.createBooking(1L, "invalid-id", 2));
    }

    @Test
    void createBooking_packageNotFound_throwsException() {
        when(userRepository.findByKeycloakId("test-keycloak-id")).thenReturn(Optional.of(user));
        when(touristPackageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                bookingService.createBooking(99L, "test-keycloak-id", 2));
    }

    @Test
    void createBooking_packageNotAvailable_throwsException() {
        pkg.setStatus(soldOutStatus);
        when(userRepository.findByKeycloakId("test-keycloak-id")).thenReturn(Optional.of(user));
        when(touristPackageRepository.findById(1L)).thenReturn(Optional.of(pkg));

        assertThrows(RuntimeException.class, () ->
                bookingService.createBooking(1L, "test-keycloak-id", 2));
    }

    @Test
    void createBooking_notEnoughSlots_throwsException() {
        pkg.setAvailableSlots(1);
        when(userRepository.findByKeycloakId("test-keycloak-id")).thenReturn(Optional.of(user));
        when(touristPackageRepository.findById(1L)).thenReturn(Optional.of(pkg));

        assertThrows(RuntimeException.class, () ->
                bookingService.createBooking(1L, "test-keycloak-id", 5));
    }

    @Test
    void createBooking_invalidPassengerCount_throwsException() {
        when(userRepository.findByKeycloakId("test-keycloak-id")).thenReturn(Optional.of(user));
        when(touristPackageRepository.findById(1L)).thenReturn(Optional.of(pkg));

        assertThrows(RuntimeException.class, () ->
                bookingService.createBooking(1L, "test-keycloak-id", 0));
    }

    @Test
    void createBooking_lastSlot_packageBecomesSoldOut() {
        pkg.setAvailableSlots(2);
        when(userRepository.findByKeycloakId("test-keycloak-id")).thenReturn(Optional.of(user));
        when(touristPackageRepository.findById(1L)).thenReturn(Optional.of(pkg));
        when(promotionRepository.findActivePromotionsByPackageId(any(), any())).thenReturn(new ArrayList<>());
        when(bookingRepository.countConfirmedBookingsByUser(user)).thenReturn(0L);
        when(statusRepository.findByNameAndEntityType("PENDING_PAYMENT", "BOOKING")).thenReturn(Optional.of(pendingStatus));
        when(statusRepository.findByNameAndEntityType("SOLD_OUT", "PACKAGE")).thenReturn(Optional.of(soldOutStatus));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(touristPackageRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        bookingService.createBooking(1L, "test-keycloak-id", 2);

        assertEquals("SOLD_OUT", pkg.getStatus().getName());
    }

    @Test
    void createBooking_withGroupDiscount() {
        PromotionEntity promo = new PromotionEntity();
        promo.setId(1L);
        promo.setName("Descuento grupo");
        promo.setDiscountPercentage(new BigDecimal("10"));
        promo.setMinPassengers(4);
        promo.setIsAccumulable(true);

        when(userRepository.findByKeycloakId("test-keycloak-id")).thenReturn(Optional.of(user));
        when(touristPackageRepository.findById(1L)).thenReturn(Optional.of(pkg));
        when(promotionRepository.findActivePromotionsByPackageId(any(), any())).thenReturn(List.of(promo));
        when(bookingRepository.countConfirmedBookingsByUser(user)).thenReturn(0L);
        when(bookingRepository.findByUser(user)).thenReturn(new ArrayList<>());
        when(statusRepository.findByNameAndEntityType("PENDING_PAYMENT", "BOOKING")).thenReturn(Optional.of(pendingStatus));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(touristPackageRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BookingEntity result = bookingService.createBooking(1L, "test-keycloak-id", 4);

        assertEquals(new BigDecimal("40.00"), result.getDiscountAmount());
        assertEquals(new BigDecimal("360.00"), result.getFinalAmount());
    }

    @Test
    void createBooking_discountExceedsMaxLimit() {
        PromotionEntity promo1 = new PromotionEntity();
        promo1.setDiscountPercentage(new BigDecimal("15"));
        promo1.setMinPassengers(2);
        promo1.setIsAccumulable(true);

        PromotionEntity promo2 = new PromotionEntity();
        promo2.setDiscountPercentage(new BigDecimal("15"));
        promo2.setMinBookingsHistory(0);
        promo2.setIsAccumulable(true);

        when(userRepository.findByKeycloakId("test-keycloak-id")).thenReturn(Optional.of(user));
        when(touristPackageRepository.findById(1L)).thenReturn(Optional.of(pkg));
        when(promotionRepository.findActivePromotionsByPackageId(any(), any())).thenReturn(List.of(promo1, promo2));
        when(bookingRepository.countConfirmedBookingsByUser(user)).thenReturn(0L);
        when(bookingRepository.findByUser(user)).thenReturn(new ArrayList<>());
        when(statusRepository.findByNameAndEntityType("PENDING_PAYMENT", "BOOKING")).thenReturn(Optional.of(pendingStatus));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(touristPackageRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BookingEntity result = bookingService.createBooking(1L, "test-keycloak-id", 2);

        // Max descuento 20%
        assertEquals(new BigDecimal("40.00"), result.getDiscountAmount());
    }

    // ===== updateBooking =====

    @Test
    void updateBooking_success() {
        BookingEntity booking = new BookingEntity();
        booking.setId(1L);
        booking.setStatus(pendingStatus);
        booking.setPassengerCount(2);
        booking.setTouristPackage(pkg);
        booking.setBaseAmount(new BigDecimal("200.00"));
        booking.setDiscountAmount(BigDecimal.ZERO);
        booking.setFinalAmount(new BigDecimal("200.00"));

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(touristPackageRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BookingEntity result = bookingService.updateBooking(1L, confirmedStatus, 2);

        assertEquals("CONFIRMED", result.getStatus().getName());
    }

    @Test
    void updateBooking_cancelledStatus_throwsException() {
        BookingEntity booking = new BookingEntity();
        booking.setId(1L);
        booking.setStatus(cancelledStatus);
        booking.setPassengerCount(2);
        booking.setTouristPackage(pkg);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(RuntimeException.class, () ->
                bookingService.updateBooking(1L, confirmedStatus, 2));
    }

    @Test
    void updateBooking_expiredStatus_throwsException() {
        BookingEntity booking = new BookingEntity();
        booking.setId(1L);
        booking.setStatus(pendingStatus);
        booking.setPassengerCount(2);
        booking.setTouristPackage(pkg);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        assertThrows(RuntimeException.class, () ->
                bookingService.updateBooking(1L, expiredStatus, 2));
    }

    @Test
    void updateBooking_cancel_releasesSlots() {
        pkg.setAvailableSlots(8);
        BookingEntity booking = new BookingEntity();
        booking.setId(1L);
        booking.setStatus(pendingStatus);
        booking.setPassengerCount(2);
        booking.setTouristPackage(pkg);
        booking.setBaseAmount(new BigDecimal("200.00"));
        booking.setDiscountAmount(BigDecimal.ZERO);
        booking.setFinalAmount(new BigDecimal("200.00"));

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(touristPackageRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        bookingService.updateBooking(1L, cancelledStatus, 2);

        assertEquals(10, pkg.getAvailableSlots());
    }

    @Test
    void updateBooking_bookingNotFound_throwsException() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                bookingService.updateBooking(99L, confirmedStatus, 2));
    }
}
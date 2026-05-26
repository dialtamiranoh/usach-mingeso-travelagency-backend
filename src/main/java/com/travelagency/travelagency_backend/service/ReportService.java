package com.travelagency.travelagency_backend.service;

import com.travelagency.travelagency_backend.repository.BookingRepository;
import com.travelagency.travelagency_backend.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final BookingRepository bookingRepository;

    public List<Map<String, Object>> getSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate))
            throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha de término");

        return bookingRepository.getSalesReport(startDate, endDate).stream()
                .map(row -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("bookingId", row[0]);
                    map.put("operationDate", row[1]);
                    map.put("clientName", row[2]);
                    map.put("packageName", row[3]);
                    map.put("passengerCount", row[4]);
                    map.put("totalAmount", row[5]);
                    map.put("bookingStatus", row[6]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPackageRanking(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate))
            throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha de término");

        return bookingRepository.getPackageRanking(startDate, endDate).stream()
                .map(row -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("packageId", row[0]);
                    map.put("packageName", row[1]);
                    map.put("destination", row[2]);
                    map.put("totalBookings", row[3]);
                    map.put("totalPassengers", row[4]);
                    map.put("totalAmount", row[5]);
                    return map;
                })
                .collect(Collectors.toList());
    }

}

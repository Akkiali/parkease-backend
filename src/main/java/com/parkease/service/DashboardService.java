package com.parkease.service;

import org.springframework.stereotype.Service;

import com.parkease.dto.DashboardResponse;
import com.parkease.repository.BookingRepository;
import com.parkease.repository.LocationRepository;
import com.parkease.repository.ParkingSlotRepository;
import com.parkease.repository.UserRepository;
import com.parkease.entity.Booking;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final BookingRepository bookingRepository;

    public DashboardService(
            UserRepository userRepository,
            LocationRepository locationRepository,
            ParkingSlotRepository parkingSlotRepository,
            BookingRepository bookingRepository) {

        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.parkingSlotRepository = parkingSlotRepository;
        this.bookingRepository = bookingRepository;
    }

    public DashboardResponse getStats() {

        DashboardResponse response =
                new DashboardResponse();

        response.setTotalUsers(
                userRepository.count());

        response.setTotalLocations(
                locationRepository.count());

        response.setTotalSlots(
                parkingSlotRepository.count());

        response.setAvailableSlots(
                parkingSlotRepository
                        .countByStatus("AVAILABLE"));

        response.setBookedSlots(
                parkingSlotRepository
                        .countByStatus("BOOKED"));

        response.setTotalBookings(
                bookingRepository.count());

        response.setCancelledBookings(
                bookingRepository
                        .countByBookingStatus(
                                "CANCELLED"));

        double revenue = bookingRepository.findAll()
                .stream()
                .filter(b -> "BOOKED"
                        .equals(b.getBookingStatus()))
                .mapToDouble(Booking::getAmount)
                .sum();

        response.setTotalRevenue(revenue);

        return response;
    }
}
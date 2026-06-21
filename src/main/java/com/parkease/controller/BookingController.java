package com.parkease.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.parkease.dto.BookingRequest;
import com.parkease.dto.BookingResponse;
import com.parkease.entity.Booking;
import com.parkease.service.BookingService;



@RestController
@RequestMapping("/api/bookings")
@CrossOrigin("*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(
            BookingService bookingService) {

        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponse bookSlot(
            @RequestBody BookingRequest request) {

        return bookingService.bookSlot(request);
    }

    @GetMapping
    public List<Booking> getAllBookings() {

        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Booking getBookingById(
            @PathVariable Long id) {

        return bookingService.getBookingById(id);
    }

    @PutMapping("/cancel/{id}")
    public String cancelBooking(
            @PathVariable Long id) {

        return bookingService.cancelBooking(id);
    }
    
    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUser(
            @PathVariable Long userId) {

        return bookingService.getBookingsByUser(userId);
    }
    
    @PutMapping("/checkout/{id}")
    public Booking checkoutBooking(
            @PathVariable Long id) {

        return bookingService.checkoutBooking(id);
    }
}
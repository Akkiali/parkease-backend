package com.parkease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parkease.entity.Booking;
import com.parkease.entity.User;

public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    List<Booking> findByUser(User user);
    
    long countByBookingStatus(String bookingStatus);
}
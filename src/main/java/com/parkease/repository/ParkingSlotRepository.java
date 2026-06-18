package com.parkease.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parkease.entity.Location;
import com.parkease.entity.ParkingSlot;

public interface ParkingSlotRepository
        extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByLocation(Location location);

    List<ParkingSlot> findByStatus(String status);

    List<ParkingSlot> findByLocationAndStatus(
            Location location,
            String status);
    
    long countByStatus(String status);
}
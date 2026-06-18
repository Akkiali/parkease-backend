package com.parkease.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.parkease.entity.ParkingSlot;
import com.parkease.repository.ParkingSlotRepository;

@Service
public class ParkingSlotService {

    private final ParkingSlotRepository parkingSlotRepository;

    public ParkingSlotService(
            ParkingSlotRepository parkingSlotRepository) {

        this.parkingSlotRepository =
                parkingSlotRepository;
    }

    public List<ParkingSlot> getAllSlots() {

        return parkingSlotRepository.findAll();
    }

    public ParkingSlot saveSlot(
            ParkingSlot parkingSlot) {

        return parkingSlotRepository.save(
                parkingSlot);
    }

    public ParkingSlot getSlotById(
            Long id) {

        return parkingSlotRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Slot Not Found"));
    }

    public ParkingSlot updateSlot(
            Long id,
            ParkingSlot slot) {

        ParkingSlot existing =
                parkingSlotRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Slot Not Found"));

        existing.setSlotNumber(
                slot.getSlotNumber());

        existing.setStatus(
                slot.getStatus());

        existing.setLocation(
                slot.getLocation());

        return parkingSlotRepository.save(
                existing);
    }

    public void deleteSlot(
            Long id) {

        parkingSlotRepository.deleteById(id);
    }
}
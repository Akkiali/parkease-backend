package com.parkease.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.parkease.entity.ParkingSlot;
import com.parkease.service.ParkingSlotService;

@RestController
@RequestMapping("/api/slots")
@CrossOrigin("*")
public class ParkingSlotController {

    private final ParkingSlotService parkingSlotService;

    public ParkingSlotController(
            ParkingSlotService parkingSlotService) {

        this.parkingSlotService =
                parkingSlotService;
    }

    @GetMapping
    public List<ParkingSlot> getAllSlots() {

        return parkingSlotService.getAllSlots();
    }

    @PostMapping
    public ParkingSlot addSlot(
            @RequestBody ParkingSlot slot) {

        return parkingSlotService.saveSlot(
                slot);
    }

    @GetMapping("/{id}")
    public ParkingSlot getSlotById(
            @PathVariable Long id) {

        return parkingSlotService.getSlotById(
                id);
    }

    @PutMapping("/{id}")
    public ParkingSlot updateSlot(
            @PathVariable Long id,
            @RequestBody ParkingSlot slot) {

        return parkingSlotService.updateSlot(
                id,
                slot);
    }

    @DeleteMapping("/{id}")
    public String deleteSlot(
            @PathVariable Long id) {

        parkingSlotService.deleteSlot(
                id);

        return "Slot Deleted Successfully";
    }
}
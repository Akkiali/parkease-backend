package com.parkease.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.parkease.entity.Location;
import com.parkease.service.LocationService;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin("origins=http://localhost:5173")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @PostMapping
    public Location addLocation(
            @RequestBody Location location) {

        return locationService.saveLocation(location);
    }
    
    @PutMapping("/{id}")
    public Location updateLocation(
            @PathVariable Long id,
            @RequestBody Location location) {

        return locationService.updateLocation(
                id,
                location);
    }

    @DeleteMapping("/{id}")
    public String deleteLocation(
            @PathVariable Long id) {

        locationService.deleteLocation(id);

        return "Location Deleted Successfully";
    }
}
/*
 * Purpose:
 * Handles Location CRUD operations.
 */

package com.parkease.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.parkease.entity.Location;
import com.parkease.repository.LocationRepository;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(
            LocationRepository locationRepository) {

        this.locationRepository =
                locationRepository;
    }

    public List<Location> getAllLocations() {

        return locationRepository.findAll();
    }

    public Location saveLocation(
            Location location) {

        return locationRepository.save(location);
    }

    public Location updateLocation(
            Long id,
            Location location) {

        Location existing =
                locationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Location Not Found"));

        existing.setStationName(
                location.getStationName());

        existing.setAddress(
                location.getAddress());

        existing.setTwoWheelerPrice(
                location.getTwoWheelerPrice());

        existing.setFourWheelerPrice(
                location.getFourWheelerPrice());

        return locationRepository.save(
                existing);
    }

    public void deleteLocation(
            Long id) {

        locationRepository.deleteById(id);
    }
}
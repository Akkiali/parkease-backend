package com.parkease.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parkease.entity.Location;

public interface LocationRepository
        extends JpaRepository<Location, Long> {

}
package com.parkease.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stationName;

    private String address;

    private Double fourWheelerPrice;

    private Double twoWheelerPrice;

    private Integer totalSlots;

    private Integer availableSlots;

    private Boolean active;

    public Location() {
    }

    public Location(Long id, String stationName, String address,
                    Double fourWheelerPrice, Double twoWheelerPrice,
                    Integer totalSlots, Integer availableSlots,
                    Boolean active) {
        this.id = id;
        this.stationName = stationName;
        this.address = address;
        this.fourWheelerPrice = fourWheelerPrice;
        this.twoWheelerPrice = twoWheelerPrice;
        this.totalSlots = totalSlots;
        this.availableSlots = availableSlots;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getFourWheelerPrice() {
        return fourWheelerPrice;
    }

    public void setFourWheelerPrice(Double fourWheelerPrice) {
        this.fourWheelerPrice = fourWheelerPrice;
    }

    public Double getTwoWheelerPrice() {
        return twoWheelerPrice;
    }

    public void setTwoWheelerPrice(Double twoWheelerPrice) {
        this.twoWheelerPrice = twoWheelerPrice;
    }

    public Integer getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(Integer totalSlots) {
        this.totalSlots = totalSlots;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
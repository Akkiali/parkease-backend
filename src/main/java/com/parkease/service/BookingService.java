package com.parkease.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.parkease.dto.BookingRequest;
import com.parkease.dto.BookingResponse;
import com.parkease.entity.Booking;
import com.parkease.entity.Location;
import com.parkease.entity.ParkingSlot;
import com.parkease.entity.User;
import com.parkease.repository.BookingRepository;
import com.parkease.repository.LocationRepository;
import com.parkease.repository.ParkingSlotRepository;
import com.parkease.repository.UserRepository;
import java.util.List;
import java.time.Duration;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final EmailService emailService;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            LocationRepository locationRepository,
            ParkingSlotRepository parkingSlotRepository,
            EmailService emailService) {

        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.parkingSlotRepository = parkingSlotRepository;
        this.emailService = emailService;
    }

    public BookingResponse bookSlot(
            BookingRequest request) {

        User user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        Location location = locationRepository
                .findById(request.getLocationId())
                .orElseThrow(() ->
                        new RuntimeException("Location Not Found"));

        ParkingSlot slot = parkingSlotRepository
                .findById(request.getSlotId())
                .orElseThrow(() ->
                        new RuntimeException("Slot Not Found"));

        if ("BOOKED".equals(slot.getStatus())) {
            throw new RuntimeException(
                    "Slot Already Booked");
        }

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setLocation(location);
        booking.setParkingSlot(slot);

        booking.setVehicleNumber(
                request.getVehicleNumber());

        booking.setVehicleType(
                request.getVehicleType());

        

        booking.setStartTime(
                LocalDateTime.now());

        booking.setEndTime(
                LocalDateTime.now().plusHours(1));

        if ("CAR".equalsIgnoreCase(
                request.getVehicleType())) {

            booking.setAmount(
                    location.getFourWheelerPrice());

        } else {

            booking.setAmount(
                    location.getTwoWheelerPrice());
        }

        booking.setBookingStatus(
                "ACTIVE");

        Booking savedBooking =
                bookingRepository.save(booking);

        slot.setStatus("ACTIVE");

        parkingSlotRepository.save(slot);
        
        if (location.getAvailableSlots() > 0) {

            location.setAvailableSlots(
                    location.getAvailableSlots() - 1);

            locationRepository.save(location);
        }
        
        String subject =
                "Parking Booking Confirmation";

        String body =
                "Hello " + user.getFullName() + ",\n\n" +

                "Your parking slot has been booked successfully.\n\n" +

                "Booking ID: " + savedBooking.getId() + "\n" +
                "Station: " + location.getStationName() + "\n" +
                "Slot Number: " + slot.getSlotNumber() + "\n" +
                "Vehicle Number: " + savedBooking.getVehicleNumber() + "\n" +
                "Vehicle Type: " + savedBooking.getVehicleType() + "\n" +
                "Amount: ₹" + savedBooking.getAmount() + "\n\n" +

                "Thank you for using ParkEase.";

        emailService.sendBookingConfirmation(
                user.getEmail(),
                subject,
                body);

        BookingResponse response =
                new BookingResponse();

        response.setBookingId(
                savedBooking.getId());

        response.setVehicleNumber(
                savedBooking.getVehicleNumber());

        response.setVehicleType(
                savedBooking.getVehicleType());

        response.setStationName(
                location.getStationName());

        response.setSlotNumber(
                slot.getSlotNumber());

        response.setBookingDate(
                savedBooking.getBookingDate());

        response.setStartTime(
                savedBooking.getStartTime());

        response.setEndTime(
                savedBooking.getEndTime());

        response.setAmount(
                savedBooking.getAmount());

        response.setBookingStatus(
                savedBooking.getBookingStatus());

        return response;
    }
    
    public List<Booking> getAllBookings() {

        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking Not Found"));
    }

    public String cancelBooking(Long id) {

        Booking booking =
                bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking Not Found"));

        booking.setBookingStatus(
                "CANCELLED");

        ParkingSlot slot =
                booking.getParkingSlot();

        slot.setStatus(
                "AVAILABLE");

        parkingSlotRepository.save(slot);

        bookingRepository.save(booking);
        
        Location location =
                booking.getLocation();

        location.setAvailableSlots(
                location.getAvailableSlots() + 1);

        locationRepository.save(location);
        
        String subject =
                "Parking Booking Cancelled";

        String body =
                "Hello " + booking.getUser().getFullName() + ",\n\n" +
                "Your parking booking has been cancelled successfully.\n\n" +
                "Booking ID: " + booking.getId() + "\n" +
                "Station: " + booking.getLocation().getStationName() + "\n" +
                "Slot Number: " + booking.getParkingSlot().getSlotNumber() + "\n\n" +
                "Thank you for using ParkEase.";

        emailService.sendEmail(
                booking.getUser().getEmail(),
                subject,
                body);

        return "Booking Cancelled Successfully";
    }
    
    public List<Booking> getBookingsByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User Not Found"));

        return bookingRepository.findByUser(user);
    }
    
    public Booking checkoutBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking Not Found"));

        booking.setEndTime(LocalDateTime.now());

        Duration duration = Duration.between(
                booking.getStartTime(),
                booking.getEndTime());

        long hours = duration.toHours();

        if (duration.toMinutesPart() > 0) {
            hours++;
        }

        if (hours == 0) {
            hours = 1;
        }

        double rate;

        if ("CAR".equalsIgnoreCase(
                booking.getVehicleType())) {

            rate = booking.getLocation()
                    .getFourWheelerPrice();

        } else {

            rate = booking.getLocation()
                    .getTwoWheelerPrice();
        }

        booking.setAmount(hours * rate);
        booking.setBookingStatus("COMPLETED");

        ParkingSlot slot = booking.getParkingSlot();
        slot.setStatus("AVAILABLE");
        parkingSlotRepository.save(slot);

        Location location = booking.getLocation();
        location.setAvailableSlots(
                location.getAvailableSlots() + 1);
        locationRepository.save(location);
        
        String subject =
                "Parking Checkout Completed";

        String body =
                "Hello " + booking.getUser().getFullName() + ",\n\n" +
                "Your parking session has been completed successfully.\n\n" +
                "Booking ID: " + booking.getId() + "\n" +
                "Station: " + booking.getLocation().getStationName() + "\n" +
                "Slot Number: " + booking.getParkingSlot().getSlotNumber() + "\n" +
                "Amount Payable: ₹" + booking.getAmount() + "\n\n" +
                "Please make payment at the parking counter.\n\n" +
                "Thank you for using ParkEase.";

        emailService.sendEmail(
                booking.getUser().getEmail(),
                subject,
                body);

        return bookingRepository.save(booking);
    }
    
}
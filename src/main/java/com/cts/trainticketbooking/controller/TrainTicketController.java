package com.cts.trainticketbooking.controller;

import com.cts.trainticketbooking.dto.TicketBookingRequest;
import com.cts.trainticketbooking.entity.*;
import com.cts.trainticketbooking.service.TrainTicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api") //context path/resource/URI(Uniform Resource Identifier)
public class TrainTicketController {

    @Autowired
    private TrainTicketService trainTicketService;

    @GetMapping("/trains")
    public ResponseEntity<List<Train>> getAllTrains() {
        List<Train> trains = trainTicketService.getAllTrains();
        return ResponseEntity.ok(trains);
    }

    @GetMapping("/trains/search")
    public ResponseEntity<List<Train>> searchTrains(@RequestParam String source, @RequestParam String destination) {
        List<Train> trains = trainTicketService.searchTrains(source, destination);
        return ResponseEntity.ok(trains);
    }

    @PostMapping("/trains")
    public ResponseEntity<Train> addTrain(@Valid @RequestBody Train train) {
        Train savedTrain = trainTicketService.addTrain(train);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrain);
    }

    @PostMapping("/bookings")//URI
    public ResponseEntity<TicketBooking> bookTicket(@Valid @RequestBody TicketBookingRequest bookingRequest) {
        TicketBooking ticketBooking = trainTicketService.bookTicket(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketBooking);
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        trainTicketService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users")
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        User savedUser = trainTicketService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}

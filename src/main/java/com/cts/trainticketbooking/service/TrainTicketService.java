package com.cts.trainticketbooking.service;
import com.cts.trainticketbooking.exception.*;

import com.cts.trainticketbooking.dto.TicketBookingRequest;
import com.cts.trainticketbooking.entity.*;
import com.cts.trainticketbooking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainTicketService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketBookingRepository ticketBookingRepository;
    
   

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    public List<Train> searchTrains(String source, String destination) {
        List<Train> trains = trainRepository.findBySourceAndDestination(source, destination);
        if (trains.isEmpty()) {
            throw new ResourceNotFoundException("No trains found for the source: " + source + " and destination: " + destination);
        }
        return trains;
    }
    
    public Train addTrain(Train train) {
        if (train.getTotalSeats() <= 0) {
            throw new InvalidRequestException("Total seats must be greater than zero");
        }
        return trainRepository.save(train);
    }

    public List<TicketBooking> getBookingsByUser(Long userId) {
        return ticketBookingRepository.findByUserId(userId);
    }

    
    public TicketBooking bookTicket(TicketBookingRequest bookingRequest) {
        if (bookingRequest.getTravelDate().isBefore(LocalDate.now())) {
            throw new InvalidRequestException("Travel date cannot be in the past");
        }

        Train train = trainRepository.findById(bookingRequest.getTrainId())
            .orElseThrow(() -> new ResourceNotFoundException("Train not found"));

        if (train.getTotalSeats() < bookingRequest.getNumberOfSeats()) {
            throw new ResourceNotFoundException("Not enough seats available");
        }

        User user = userRepository.findById(bookingRequest.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TicketBooking ticketBooking = new TicketBooking();
        ticketBooking.setTrain(train);
        ticketBooking.setUser(user);
        ticketBooking.setTravelDate(bookingRequest.getTravelDate());
        ticketBooking.setNumberOfSeats(bookingRequest.getNumberOfSeats());

        train.setTotalSeats(train.getTotalSeats() - bookingRequest.getNumberOfSeats());
        trainRepository.save(train);

        return ticketBookingRepository.save(ticketBooking);
    }


    public void cancelBooking(Long id) {
        TicketBooking booking = ticketBookingRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (booking.getTravelDate().isBefore(LocalDate.now())) {
            throw new InvalidRequestException("Tickets cannot be canceled for past or same-day travel");
        }

        Train train = booking.getTrain();
        train.setTotalSeats(train.getTotalSeats() + booking.getNumberOfSeats());
        trainRepository.save(train);

        ticketBookingRepository.deleteById(id);
    }


      public User saveUser(User user) {
       return userRepository.save(user);
    }
   
  

   
}

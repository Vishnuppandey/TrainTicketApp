package com.cts.trainticketbooking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TicketBookingRequest {

    @NotNull(message = "Train ID is required")
    private Long trainId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Travel date is required")
    private LocalDate travelDate;

    @Min(value = 1, message = "Number of seats must be at least 1")
    private int numberOfSeats;
}
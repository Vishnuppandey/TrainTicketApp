package com.cts.trainticketbooking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Train {

    private Long id; // Include the ID for cases where it's needed, like updates or responses.

    @NotBlank(message = "Train name is required")
    private String name;

    @NotBlank(message = "Source station is required")
    private String source;

    @NotBlank(message = "Destination station is required")
    private String destination;

    @Min(value = 1, message = "Number of seats must be at least 1")
    private int totalSeats;
}
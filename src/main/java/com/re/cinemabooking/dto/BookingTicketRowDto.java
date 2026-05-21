package com.re.cinemabooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingTicketRowDto {
    private Long bookingId;
    private LocalDateTime bookingDate;
    private Double totalAmount;
    private String status;
    private String movieTitle;
    private LocalDateTime startTime;
    private String roomName;
    private String seatName;
    private Double price;
}

package com.re.cinemabooking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BookingHistoryDto {
    private Long bookingId;
    private LocalDateTime bookingDate;
    private Double totalAmount;
    private String status;
    private String movieTitle;
    private LocalDateTime startTime;
    private String roomName;
    private List<String> seatNames = new ArrayList<>();
    private boolean cancellable;

    public String getSeatLabel() {
        return String.join(", ", seatNames);
    }
}

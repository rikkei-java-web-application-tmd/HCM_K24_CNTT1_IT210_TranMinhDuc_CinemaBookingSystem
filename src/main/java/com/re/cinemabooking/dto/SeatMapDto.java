package com.re.cinemabooking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class SeatMapDto {
    private Long showtimeId;
    private String movieTitle;
    private String roomName;
    private LocalDateTime startTime;
    private Integer totalSeats;
    private Long soldSeats;
    private boolean soldOut;
    private List<SeatStatusDto> seats = new ArrayList<>();
}

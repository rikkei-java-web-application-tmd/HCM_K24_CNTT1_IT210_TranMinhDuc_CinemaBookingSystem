package com.re.cinemabooking.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BookingRequestDto {
    private Long showtimeId;
    private List<Long> seatIds = new ArrayList<>();
}

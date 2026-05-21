package com.re.cinemabooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatStatusDto {
    private Long seatId;
    private String seatName;
    private boolean sold;
}

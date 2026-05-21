package com.re.cinemabooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeListDto {
    private Long id;
    private String movieTitle;
    private String roomName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

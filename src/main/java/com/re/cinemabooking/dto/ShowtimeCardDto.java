package com.re.cinemabooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowtimeCardDto {
    private Long id;
    private String movieTitle;
    private String posterUrl;
    private Integer duration;
    private String roomName;
    private LocalDateTime startTime;
    private Integer totalSeats;
    private Long soldSeats;
    private boolean soldOut;

    public ShowtimeCardDto(Long id, String movieTitle, String posterUrl, Integer duration,
                           String roomName, LocalDateTime startTime, Integer totalSeats,
                           Long soldSeats) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.posterUrl = posterUrl;
        this.duration = duration;
        this.roomName = roomName;
        this.startTime = startTime;
        this.totalSeats = totalSeats;
        this.soldSeats = soldSeats == null ? 0L : soldSeats;
        this.soldOut = totalSeats != null && this.soldSeats >= totalSeats;
    }
}

package com.re.cinemabooking.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MovieFormDto {
    private Long id;
    private String title;
    private String description;
    private String director;
    private Integer duration;
    private String posterUrl;
    private String status = "ACTIVE";
    private List<Long> genreIds = new ArrayList<>();
}

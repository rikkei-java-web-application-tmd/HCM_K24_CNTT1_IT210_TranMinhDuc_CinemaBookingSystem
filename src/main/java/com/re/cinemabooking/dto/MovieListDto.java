package com.re.cinemabooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieListDto {
    private Long id;
    private String title;
    private String description;
    private String director;
    private Integer duration;
    private String posterUrl;
    private String status;
    private List<String> genreNames;
}

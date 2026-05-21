package com.re.cinemabooking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieOptionDto {
    private Long id;
    private String title;
    private Integer duration;
}

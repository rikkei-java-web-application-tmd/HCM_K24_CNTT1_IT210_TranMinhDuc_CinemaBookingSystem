package com.re.cinemabooking.dto;

import lombok.Data;

@Data
public class ProfileDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String role;
}

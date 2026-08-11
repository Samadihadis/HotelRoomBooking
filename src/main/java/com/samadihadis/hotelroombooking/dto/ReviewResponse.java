package com.samadihadis.hotelroombooking.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private Long id;
    private Long rate;
    private String comment;
    private LocalDate createAt;

    private Long userId;
    private String userFullName;

    private Long hotelId;
    private String hotelName;
}
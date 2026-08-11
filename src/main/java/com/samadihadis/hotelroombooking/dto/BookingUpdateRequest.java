package com.samadihadis.hotelroombooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingUpdateRequest {

    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Integer guestCount;
}

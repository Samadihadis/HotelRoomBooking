package com.samadihadis.hotelroombooking.dto;

import com.samadihadis.hotelroombooking.enumes.BookingState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long id;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Integer guestCount;
    private BigDecimal totalPrice;
    private BookingState bookingState;
    private LocalDateTime reserveDate;

    private Long userId;
    private String userFullName;
    private Long roomId;
    private String roomNumber;
}

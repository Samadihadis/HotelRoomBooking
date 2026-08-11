package com.samadihadis.hotelroombooking.dto.room;


import com.samadihadis.hotelroombooking.enumes.RoomState;
import com.samadihadis.hotelroombooking.enumes.RoomType;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {

    private Long id;
    private Long roomNumber;
    private RoomType roomType;
    private RoomState roomState;
    private BigDecimal basePrice;
    private Integer maxCapacity;
    private String description;

    private Long hotelId;
    private String hotelName;
}
package com.samadihadis.hotelroombooking.dto.room;


import com.samadihadis.hotelroombooking.enumes.RoomState;
import com.samadihadis.hotelroombooking.enumes.RoomType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdateRequest {

    private Long roomNumber;
    private RoomType roomType;
    private RoomState roomState;

    @DecimalMin(value = "0.0", inclusive = false, message = "قیمت باید بیشتر از صفر باشد.")
    private BigDecimal basePrice;

    @Min(value = 1, message = "ظرفیت حداقل ۱ نفر است.")
    private Integer maxCapacity;

    private String description;
}

package com.samadihadis.hotelroombooking.dto.room;


import com.samadihadis.hotelroombooking.enumes.RoomState;
import com.samadihadis.hotelroombooking.enumes.RoomType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateRequest {

    @NotNull(message = "شماره اتاق الزامی است.")
    private Long roomNumber;

    @NotNull(message = "نوع اتاق الزامی است.")
    private RoomType roomType;

    @NotNull(message = "وضعیت اتاق الزامی است.")
    private RoomState roomState;

    @NotNull(message = "قیمت پایه الزامی است.")
    @DecimalMin(value = "0.0", inclusive = false, message = "قیمت باید بیشتر از صفر باشد.")
    private BigDecimal basePrice;

    @NotNull(message = "ظرفیت اتاق الزامی است.")
    @Min(value = 1, message = "ظرفیت حداقل ۱ نفر است.")
    private Integer maxCapacity;

    private String description;

    @NotNull(message = "شناسه هتل الزامی است.")
    private Long hotelId;
}

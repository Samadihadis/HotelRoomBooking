package com.samadihadis.hotelroombooking.dto.hoteldto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelCreateRequest {

    @NotBlank(message = "نام هتل نمی‌تواند خالی باشد.")
    private String name;

    private String address;

    private String description;

    @Min(value = 0, message = "امتیاز نمی‌تواند منفی باشد.")
    @Max(value = 5, message = "امتیاز حداکثر ۵ است.")
    private Integer rate;

    @Min(value = 1, message = "ستاره هتل حداقل ۱ است.")
    @Max(value = 5, message = "ستاره هتل حداکثر ۵ است.")
    private Integer starRating;
}

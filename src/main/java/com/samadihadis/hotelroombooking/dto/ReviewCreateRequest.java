package com.samadihadis.hotelroombooking.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {

    @NotNull(message = "امتیاز الزامی است.")
    @Min(value = 1, message = "امتیاز باید حداقل ۱ باشد.")
    @Max(value = 5, message = "امتیاز باید حداکثر ۵ باشد.")
    private Long rate;

    @NotBlank(message = "متن نظر نمی‌تواند خالی باشد.")
    private String comment;

    @NotNull(message = "شناسه کاربر الزامی است.")
    private Long userId;

    @NotNull(message = "شناسه هتل الزامی است.")
    private Long hotelId;
}
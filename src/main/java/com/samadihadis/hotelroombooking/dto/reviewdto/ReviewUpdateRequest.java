package com.samadihadis.hotelroombooking.dto.reviewdto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewUpdateRequest {

    @Min(value = 1, message = "امتیاز باید حداقل ۱ باشد.")
    @Max(value = 5, message = "امتیاز باید حداکثر ۵ باشد.")
    private Long rate;

    private String comment;
}

package com.samadihadis.hotelroombooking.dto.bookingdto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequest {

    @NotNull(message = "تاریخ ورود الزامی است.")
    @FutureOrPresent(message = "تاریخ ورود نمی‌تواند در گذشته باشد.")
    private LocalDate checkinDate;

    @NotNull(message = "تاریخ خروج الزامی است.")
    private LocalDate checkoutDate;

    @NotNull(message = "تعداد مهمان الزامی است.")
    @Min(value = 1, message = "حداقل یک مهمان باید باشد.")
    private Integer guestCount;

    @NotNull(message = "شناسه کاربر الزامی است.")
    private Long userId;

    @NotNull(message = "شناسه اتاق الزامی است.")
    private Long roomId;
}

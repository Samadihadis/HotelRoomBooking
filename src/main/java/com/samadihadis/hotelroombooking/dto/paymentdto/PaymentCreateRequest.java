package com.samadihadis.hotelroombooking.dto.paymentdto;

import com.samadihadis.hotelroombooking.enumes.PaymentMethod;
import com.samadihadis.hotelroombooking.enumes.PaymentState;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequest {

    @NotNull(message = "مبلغ پرداخت الزامی است.")
    @DecimalMin(value = "0.01", message = "مبلغ پرداخت باید بیشتر از صفر باشد.")
    private BigDecimal amount;

    @NotNull(message = "روش پرداخت الزامی است.")
    private PaymentMethod paymentMethod;

    @NotNull(message = "وضعیت پرداخت الزامی است.")
    private PaymentState paymentState;

    private String trackingCode;

    @NotNull(message = "شناسه رزرو الزامی است.")
    private Long bookingId;
}

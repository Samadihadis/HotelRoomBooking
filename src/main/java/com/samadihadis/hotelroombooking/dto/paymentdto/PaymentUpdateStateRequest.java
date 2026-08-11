package com.samadihadis.hotelroombooking.dto.paymentdto;

import com.samadihadis.hotelroombooking.enumes.PaymentState;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateStateRequest {

    @NotNull(message = "وضعیت پرداخت الزامی است.")
    private PaymentState paymentState;
}

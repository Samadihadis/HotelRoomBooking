package com.samadihadis.hotelroombooking.dto.paymentdto;

import com.samadihadis.hotelroombooking.enumes.PaymentMethod;
import com.samadihadis.hotelroombooking.enumes.PaymentState;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentState paymentState;
    private LocalDate paymentDate;
    private String trackingCode;

    private Long bookingId;
}
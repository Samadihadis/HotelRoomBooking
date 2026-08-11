package com.samadihadis.hotelroombooking.mapper;


import com.samadihadis.hotelroombooking.dto.paymentdto.PaymentCreateRequest;
import com.samadihadis.hotelroombooking.dto.paymentdto.PaymentResponse;
import com.samadihadis.hotelroombooking.entity.Payment;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    @Mapping(target = "booking", ignore = true)
    Payment toEntity(PaymentCreateRequest request);

    @Mapping(target = "bookingId", source = "booking.id")
    PaymentResponse toResponse(Payment payment);
}

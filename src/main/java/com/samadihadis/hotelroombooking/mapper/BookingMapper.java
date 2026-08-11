package com.samadihadis.hotelroombooking.mapper;

import com.samadihadis.hotelroombooking.dto.bookingdto.BookingCreateRequest;
import com.samadihadis.hotelroombooking.dto.bookingdto.BookingResponse;
import com.samadihadis.hotelroombooking.dto.bookingdto.BookingUpdateRequest;
import com.samadihadis.hotelroombooking.entity.Booking;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "bookingState", ignore = true)
    @Mapping(target = "reserveDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "payment", ignore = true)
    Booking toEntity(BookingCreateRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userFullName", source = "user.fullName")
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "roomNumber", source = "room.roomNumber")
    BookingResponse toResponse(Booking booking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(BookingUpdateRequest request, @MappingTarget Booking booking);
}
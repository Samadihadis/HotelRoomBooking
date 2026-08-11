package com.samadihadis.hotelroombooking.mapper;


import com.samadihadis.hotelroombooking.dto.room.RoomCreateRequest;
import com.samadihadis.hotelroombooking.dto.room.RoomResponse;
import com.samadihadis.hotelroombooking.dto.room.RoomUpdateRequest;
import com.samadihadis.hotelroombooking.entity.Room;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    Room toEntity(RoomCreateRequest request);

    @Mapping(target = "hotelId", source = "hotel.id")
    @Mapping(target = "hotelName", source = "hotel.name")
    RoomResponse toResponse(Room room);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(RoomUpdateRequest request, @MappingTarget Room room);
}

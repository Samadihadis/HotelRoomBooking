package com.samadihadis.hotelroombooking.mapper;


import com.samadihadis.hotelroombooking.dto.HotelCreateRequest;
import com.samadihadis.hotelroombooking.dto.HotelResponse;
import com.samadihadis.hotelroombooking.dto.HotelUpdateRequest;
import com.samadihadis.hotelroombooking.entity.Hotel;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HotelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    Hotel toEntity(HotelCreateRequest request);

    HotelResponse toResponse(Hotel hotel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(HotelUpdateRequest request, @MappingTarget Hotel hotel);
}
package com.samadihadis.hotelroombooking.mapper;


import com.samadihadis.hotelroombooking.dto.reviewdto.ReviewCreateRequest;
import com.samadihadis.hotelroombooking.dto.reviewdto.ReviewResponse;
import com.samadihadis.hotelroombooking.dto.reviewdto.ReviewUpdateRequest;
import com.samadihadis.hotelroombooking.entity.Review;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "hotel", ignore = true)
    Review toEntity(ReviewCreateRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userFullName", source = "user.fullName")
    @Mapping(target = "hotelId", source = "hotel.id")
    @Mapping(target = "hotelName", source = "hotel.name")
    ReviewResponse toResponse(Review review);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ReviewUpdateRequest request, @MappingTarget Review review);
}
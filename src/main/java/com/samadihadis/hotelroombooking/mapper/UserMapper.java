package com.samadihadis.hotelroombooking.mapper;

import com.samadihadis.hotelroombooking.dto.*;
import com.samadihadis.hotelroombooking.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userState", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    User toEntity(UserRegisterRequest request);

    UserResponseDTO toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UserUpdateRequestDTO request, @MappingTarget User user);
}

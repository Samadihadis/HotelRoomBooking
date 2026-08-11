package com.samadihadis.hotelroombooking.dto.userdto;

import com.samadihadis.hotelroombooking.enumes.UserRole;
import com.samadihadis.hotelroombooking.enumes.UserState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private UserState userState;
    private UserRole userRole;
    private LocalDateTime registrationDate;
}

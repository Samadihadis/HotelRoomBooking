package com.samadihadis.hotelroombooking.dto.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

    @NotBlank(message = "نام کامل نمی‌تواند خالی باشد.")
    private String fullName;

    @NotBlank(message = "ایمیل نمی‌تواند خالی باشد.")
    @Email(message = "فرمت ایمیل معتبر نیست.")
    private String email;

    @NotBlank(message = "رمز عبور نمی‌تواند خالی باشد.")
    @Size(min = 6, message = "رمز عبور باید حداقل ۶ کاراکتر باشد.")
    private String password;

    private String phone;
}

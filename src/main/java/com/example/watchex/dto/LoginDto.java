package com.example.watchex.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotEmpty(message = "Thiếu email")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotEmpty(message = "Thiếu mật khẩu")
    private String password;
}

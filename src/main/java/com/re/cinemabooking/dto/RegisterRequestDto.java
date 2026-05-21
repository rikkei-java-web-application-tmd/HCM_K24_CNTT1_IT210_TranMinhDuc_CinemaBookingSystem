package com.re.cinemabooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @NotBlank(message = "Tên đăng nhập không được để trống.")
    @Size(min = 4, max = 30, message = "Tên đăng nhập phải từ 4 đến 30 ký tự.")
    @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "Tên đăng nhập chỉ được chứa chữ, số và dấu gạch dưới.")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống.")
    @Size(min = 6, max = 72, message = "Mật khẩu phải từ 6 đến 72 ký tự.")
    private String password;

    @NotBlank(message = "Họ tên không được để trống.")
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự.")
    private String fullName;

    @NotBlank(message = "Email không được để trống.")
    @Email(message = "Email không đúng định dạng.")
    @Size(max = 120, message = "Email không được vượt quá 120 ký tự.")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống.")
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại phải bắt đầu bằng 0 hoặc +84 và có 10-11 chữ số.")
    private String phone;
}

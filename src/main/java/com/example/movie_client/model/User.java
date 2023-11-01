package com.example.movie_client.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class User {
    @NotBlank(message = "Không được để trống email!")
    @Email(message = "Bạn cần nhập email hợp lệ!")
    private String username;

    @NotBlank(message = "Không được để trống mật khẩu!")
    @Size(min=6,message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Không được để trống họ tên!")
    private String fullName;

    private Set<Role> roles;
}

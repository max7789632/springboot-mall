package com.maxdeng.springbootmall.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterRequest {

    @NotBlank // 不得為空值或空白
    @Email
    private String email;

    @NotBlank
    private String password;
}

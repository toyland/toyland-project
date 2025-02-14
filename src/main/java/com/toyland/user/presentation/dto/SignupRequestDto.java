package com.toyland.user.presentation.dto;

import com.toyland.user.model.UserRoleEnum;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private UserRoleEnum role;
}

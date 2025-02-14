package com.toyland.user.presentation.dto;

import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupResponseDto {

    private String username;
    private String password;
    private UserRoleEnum role;


    public SignupResponseDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }
}

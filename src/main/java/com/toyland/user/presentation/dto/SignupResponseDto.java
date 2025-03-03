package com.toyland.user.presentation.dto;

import com.toyland.user.model.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupResponseDto {

    private String username;
    private UserRoleEnum role;

    @Builder
    public SignupResponseDto(String username, UserRoleEnum role) {
        this.username = username;
        this.role = role;
    }
}

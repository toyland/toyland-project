package com.toyland.user.presentation.dto;

import com.toyland.user.model.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserResponseDto {

    private String username;
    private String password;
    private UserRoleEnum role;

    @Builder
    public UpdateUserResponseDto(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}

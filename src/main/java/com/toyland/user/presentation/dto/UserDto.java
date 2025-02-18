package com.toyland.user.presentation.dto;

import com.toyland.user.model.UserRoleEnum;

public record UserDto (Long id,
                       String username,
                       String password,
                       UserRoleEnum role){

    public static UserDto of(Long id, String username, String password, UserRoleEnum role) {
        return new UserDto(id, username, password, role);
    };
}

package com.toyland.user.presentation.dto;

import com.toyland.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String username;
    private String password;
    private String role;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(user.getUsername(),
                user.getPassword(),
                user.getRole().name());
    }
}

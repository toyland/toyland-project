package com.toyland.user.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserResponseDto {

    private String username;
    private String password;

    @Builder
    public UpdateUserResponseDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

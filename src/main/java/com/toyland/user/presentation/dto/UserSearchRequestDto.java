package com.toyland.user.presentation.dto;

import com.toyland.user.model.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSearchRequestDto {
    private String username;
    private UserRoleEnum role;
    private Boolean isDeleted;

}

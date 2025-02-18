package com.toyland.user.presentation.dto;

import com.toyland.user.model.UserRoleEnum;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequestDto {

    @Size(min = 4, max = 10, message = "사용자 이름은 4자 이상 10자 이하이어야 합니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "사용자 이름은 소문자 알파벳과 숫자로만 구성되어야 합니다.")
    private String username;

    @Size(min = 8, max = 15, message = "비밀번호는 최소 8자 이상 15자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()]).{8,15}$",
            message = "비밀번호는 최소 8자 이상 15자 이하이며, 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    private UserRoleEnum role;

    @Builder
    public UpdateUserRequestDto(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void encodePassword(String password) {
        this.password = password;
    }
}

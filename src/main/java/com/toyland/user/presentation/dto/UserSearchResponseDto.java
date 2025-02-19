package com.toyland.user.presentation.dto;

import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;

import java.time.LocalDateTime;

public record UserSearchResponseDto(Long id,
                                    String username,
                                    UserRoleEnum role,
                                    Long createdBy,
                                    LocalDateTime createdAt,
                                    Long updatedBy,
                                    LocalDateTime updatedAt,
                                    Long deletedBy,
                                    LocalDateTime deletedAt) {
    public static UserSearchResponseDto from(User user) {
        return new UserSearchResponseDto(user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getCreatedBy(),
                user.getCreatedAt(),
                user.getUpdatedBy(),
                user.getUpdatedAt(),
                user.getDeletedBy(),
                user.getDeletedAt());
    }

}

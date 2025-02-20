package com.toyland.store.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateStoreRequestDto  (

    @NotNull(message = "이름은 필수 값 입니다.")
    @NotEmpty(message = "이름은 필수 값 입니다.")
    String name,

    String content,

    @NotNull(message = "주소는 필수 값 입니다.")
    @NotEmpty(message = "주소는 필수 값 입니다.")
    String address,

    UUID regionId,

    Long ownerId
){}

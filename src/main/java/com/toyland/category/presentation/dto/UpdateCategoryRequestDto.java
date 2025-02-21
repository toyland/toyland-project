package com.toyland.category.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateCategoryRequestDto (

    @NotEmpty(message = "이름은 필수 입력값입니다.")
    @NotNull(message = "이름은 필수 입력값입니다.")
    String name,

    UUID parentCategoryId
){
}

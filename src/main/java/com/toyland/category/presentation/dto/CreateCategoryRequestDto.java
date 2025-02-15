/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.UUID;

public record CreateCategoryRequestDto(
    UUID patentId,
    @NotEmpty(message = "이름은 필수 입력값입니다.") String name) {

}

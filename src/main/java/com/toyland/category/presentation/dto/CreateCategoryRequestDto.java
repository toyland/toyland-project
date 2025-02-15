/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.presentation.dto;

import java.util.UUID;

public record CreateCategoryRequestDto(UUID patentId, String name) {

}

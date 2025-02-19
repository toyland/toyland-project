package com.toyland.user.infrastructure;

import com.toyland.user.model.User;
import com.toyland.user.presentation.dto.UserSearchRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JpaUserRepositoryCustom {

    Page<User> search(UserSearchRequestDto requestDto, Pageable pageable);
}

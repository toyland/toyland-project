package com.toyland.user.application;

import com.toyland.global.config.security.UserDetailsImpl;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.UserErrorCode;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import com.toyland.user.presentation.dto.SignupRequestDto;
import com.toyland.user.presentation.dto.SignupResponseDto;
import com.toyland.user.presentation.dto.UpdateUserRequestDto;
import com.toyland.user.presentation.dto.UpdateUserResponseDto;
import com.toyland.user.presentation.dto.UserResponseDto;
import com.toyland.user.presentation.dto.UserSearchRequestDto;
import com.toyland.user.presentation.dto.UserSearchResponseDto;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 유저 정보를 저장합니다.
     *
     * @param requestDto 로그인 유저 정보
     * @return 저장된 회원정보
     */
    public SignupResponseDto signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        UserRoleEnum role = requestDto.getRole();

        if (userRepository.existsByUsername(username)) {
            throw new CustomException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userRepository.save(new User(username, password, role));

        return SignupResponseDto.builder()
            .username(user.getUsername())
            .role(user.getRole())
            .build();
    }

    public UpdateUserResponseDto updateUserInfo(UpdateUserRequestDto requestDto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() ->
            CustomException.from(UserErrorCode.USER_NOT_FOUND));

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new CustomException(UserErrorCode.USER_ALREADY_EXISTS);
        }

        requestDto.encodePassword(passwordEncoder.encode(requestDto.getPassword()));

        user.updateUser(requestDto);

        return UpdateUserResponseDto.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .build();
    }

    public void deleteUser(Long userId, UserDetailsImpl userDetail) {

        User user = userRepository.findById(userId).orElseThrow(() ->
            CustomException.from(UserErrorCode.USER_NOT_FOUND));

        if (!Objects.equals(userDetail.getUserId(), userId)) {
            if (userDetail.getRole() != UserRoleEnum.MASTER) {
                throw new IllegalArgumentException("삭제 권한이 없습니다.");
            }
        }

        user.addDeletedField(userDetail.getUserId());
    }

    @Transactional(readOnly = true)
    public UserResponseDto findbyUserId(Long userId) {
        return UserResponseDto.of(userRepository.findById(userId).orElseThrow(() ->
            CustomException.from(UserErrorCode.USER_NOT_FOUND)));
    }

    @Transactional(readOnly = true)
    public Page<UserSearchResponseDto> search(UserSearchRequestDto dto, Pageable pageable) {

        return userRepository.search(dto, pageable)
            .map(UserSearchResponseDto::from);
    }
}

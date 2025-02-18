package com.toyland.user.application;

import com.toyland.global.config.security.UserDetailsImpl;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.UserErrorCode;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import com.toyland.user.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 유저 정보를 저장합니다.
     * @param requestDto 로그인 유저 정보
     * @return 저장된 회원정보
     */
    public SignupResponseDto signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        UserRoleEnum role = requestDto.getRole();

        if (userRepository.existsByUsername(username)) {
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }

        User user = userRepository.save(new User(username, password, role));

        return SignupResponseDto.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public UpdateUserResponseDto updateUserInfo(UpdateUserRequestDto requestDto, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(()->
                CustomException.from(UserErrorCode.USER_NOT_FOUND));

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }

        requestDto.encodePassword(passwordEncoder.encode(requestDto.getPassword()));

        user.updateUser(requestDto);

        return UpdateUserResponseDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public void deleteUser(Long userId, UserDetailsImpl userDetail) {

        User user = userRepository.findById(userId).orElseThrow(()->
                        CustomException.from(UserErrorCode.USER_NOT_FOUND));

        if (!Objects.equals(userDetail.getUser().getId(), userId)) {
            if (userDetail.getRole() != UserRoleEnum.MASTER) {
                throw new IllegalArgumentException("삭제 권한이 없습니다.");
            }
        }

        user.addDeletedField(userDetail.getId());
    }


    public UserResponseDto findbyUserId(Long userId){
        return UserResponseDto.of(userRepository.findById(userId).orElseThrow(()->
                CustomException.from(UserErrorCode.USER_NOT_FOUND)));
    }

}

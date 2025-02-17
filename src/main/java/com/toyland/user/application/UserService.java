package com.toyland.user.application;

import com.toyland.global.config.security.UserDetailsImpl;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.BusinessErrorCode;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import com.toyland.user.presentation.dto.SignupRequestDto;
import com.toyland.user.presentation.dto.SignupResponseDto;
import com.toyland.user.presentation.dto.UpdateUserRequestDto;
import com.toyland.user.presentation.dto.UpdateUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

        // 사용자 이름 중복 검사
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
        }

        User user = userRepository.save(new User(username, password, role));

        return SignupResponseDto.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public UpdateUserResponseDto updateUserInfo(UpdateUserRequestDto requestDto, Long userId, UserDetailsImpl userDetail) {

        User user = findbyUserId(userId);

        String updateUsername = user.getUsername();
        String updatePassword = user.getPassword();
        UserRoleEnum updateRole = user.getRole();

        if (StringUtils.hasText(requestDto.getUsername())) {
            if (userRepository.existsByUsername(requestDto.getUsername())) {
                throw new IllegalArgumentException("이미 사용 중인 사용자 이름입니다.");
            }
            updateUsername = requestDto.getUsername();
        }

        if (StringUtils.hasText(requestDto.getPassword())) {
            updatePassword = passwordEncoder.encode(requestDto.getPassword());
        }

        if (userDetail.getRole() == UserRoleEnum.MASTER && requestDto.getRole() != null) {
            updateRole = requestDto.getRole();
        }

        user.updateUser(updateUsername, updatePassword, updateRole);

        User updatedUser = userRepository.save(user);

        return UpdateUserResponseDto.builder()
                .username(updatedUser.getUsername())
                .role(updatedUser.getRole())
                .build();
    }

    public void deleteUser(Long userId, UserDetailsImpl userDetail) {

        User user = findbyUserId(userId);

        if (!Objects.equals(userDetail.getUser().getId(), userId)) {
            if (userDetail.getRole() != UserRoleEnum.MASTER) {
                throw new IllegalArgumentException("삭제 권한이 없습니다.");
            }
        }

        Long deletedBy = userDetail.getUser().getId();
        user.addDeletedField(deletedBy);

        userRepository.save(user);
    }


    public User findbyUserId(Long userId){
        return userRepository.findById(userId).orElseThrow(()->
                new CustomException(BusinessErrorCode.USER_NOT_FOUND));
    }

}

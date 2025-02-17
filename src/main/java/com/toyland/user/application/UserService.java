package com.toyland.user.application;

import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.UserErrorCode;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import com.toyland.user.presentation.dto.SignupRequestDto;
import com.toyland.user.presentation.dto.SignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        User user = userRepository.save(new User(username, password, role));
        return new SignupResponseDto(user);
    }

    public User findbyUserId(Long userId){
        return userRepository.findById(userId).orElseThrow(()->
                CustomException.from(UserErrorCode.USER_NOT_FOUND));
    }
}

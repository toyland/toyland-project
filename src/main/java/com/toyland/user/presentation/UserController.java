package com.toyland.user.presentation;

import com.toyland.global.config.security.UserDetailsImpl;
import com.toyland.user.application.UserService;
import com.toyland.user.presentation.dto.SignupRequestDto;
import com.toyland.user.presentation.dto.SignupResponseDto;
import com.toyland.user.presentation.dto.UpdateUserRequestDto;
import com.toyland.user.presentation.dto.UpdateUserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 요첨 API
     * @param requestDto 로그인 유저 정보
     * @return 회원정보를 포함한 성공적인 응답 생성
     */
    @PostMapping("/users/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    /**
     * 회원정보 수정 API
     * @param requestDto 수정할 유저 정보
     * @return 회원정보를 포함한 성공적인 응답 생성
     */
    @PutMapping("/users/{userId}")
    public ResponseEntity<UpdateUserResponseDto> update(@Valid @RequestBody UpdateUserRequestDto requestDto,
                                                        @PathVariable Long userId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetail) {
        UpdateUserResponseDto updateUserResponseDto = userService.updateUserInfo(requestDto, userId, userDetail);
        return ResponseEntity.ok().body(updateUserResponseDto);
    }

    /**
     * 회원탈퇴 API
     * @return 성공적인 응답 생성
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetail) {
        userService.deleteUser(userId, userDetail);
        return ResponseEntity.ok().build();
    }



}

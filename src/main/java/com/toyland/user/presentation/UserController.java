package com.toyland.user.presentation;

import com.toyland.user.application.UserService;
import com.toyland.user.presentation.dto.LoginRequestDto;
import com.toyland.user.presentation.dto.SignupRequestDto;
import com.toyland.user.presentation.dto.SignupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }
    /**
     * 로그인 요청 API
     * @param requestDto 로그인 유저 ID,password
     * @return 회원정보를 포함한 성공적인 응답 생성
     */
    @PostMapping("/users/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {
        try {
            userService.login(requestDto);
            return ResponseEntity.ok().body("로그인 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

package com.toyland.user.presentation;

import com.toyland.global.config.security.UserDetailsImpl;
import com.toyland.user.application.UserService;
import com.toyland.user.presentation.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 요첨 API
     * @param requestDto 로그인 유저 정보
     * @return 회원정보를 포함한 성공적인 응답 생성
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.ok().body(responseDto);
    }
    /**
     * 회원정보 조회 API
     * @param userId 조회할 유저
     * @return 조회한 회원정보를 포함한 성공적인 응답 생성
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> search(@PathVariable Long userId) {
        UserResponseDto userResponseDto = userService.findbyUserId(userId);
        return ResponseEntity.ok().body(userResponseDto);
    }

    /**
     * 회원정보 전체 조회 API
     * @param pageable 페이지정보
     * @return 조회한 회원정보를 포함한 성공적인 응답 생성
     */
    @GetMapping("/search")
    public ResponseEntity<Page<UserSearchResponseDto>> searchUser(@RequestBody UserSearchRequestDto Dto, Pageable pageable) {
        return ResponseEntity.ok(userService.search(Dto,pageable));
    }

    /**
     * 회원정보 수정 API
     * @param requestDto 수정할 유저 정보
     * @return 회원정보를 포함한 성공적인 응답 생성
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UpdateUserResponseDto> update(@Valid @RequestBody UpdateUserRequestDto requestDto,
                                                        @PathVariable Long userId) {
        UpdateUserResponseDto updateUserResponseDto = userService.updateUserInfo(requestDto, userId);
        return ResponseEntity.ok().body(updateUserResponseDto);
    }

    /**
     * 회원탈퇴 API
     * @return 성공적인 응답 생성
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetail) {
        userService.deleteUser(userId, userDetail);
        return ResponseEntity.ok().build();
    }

}

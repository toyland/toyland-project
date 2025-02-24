package com.toyland.user.presentation;

import com.toyland.global.config.security.UserDetailsImpl;
import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotation;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotationList;
import com.toyland.global.config.swagger.response.CustomApiResponse;
import com.toyland.global.config.swagger.response.HttpSuccessCode;
import com.toyland.global.exception.type.ApiErrorCode;
import com.toyland.user.application.UserService;
import com.toyland.user.presentation.dto.SignupRequestDto;
import com.toyland.user.presentation.dto.SignupResponseDto;
import com.toyland.user.presentation.dto.UpdateUserRequestDto;
import com.toyland.user.presentation.dto.UpdateUserResponseDto;
import com.toyland.user.presentation.dto.UserResponseDto;
import com.toyland.user.presentation.dto.UserSearchRequestDto;
import com.toyland.user.presentation.dto.UserSearchResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "사용자", description = "User API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입", description = "회원가입 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원 가입 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PostMapping("/signup")
    public ResponseEntity<CustomApiResponse<SignupResponseDto>> signup(
        @Valid @RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.signup(requestDto);

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/users/login")
            .build().toUri();
        return ResponseEntity.created(uri).body(
            CustomApiResponse.of(HttpSuccessCode.USER_SIGNUP, responseDto));
    }

    @Operation(summary = "회원 단 건 조회", description = "회원 조회 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 조회 성공"),
    })
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/{userId}")
    public ResponseEntity<CustomApiResponse<UserResponseDto>> search(@PathVariable Long userId) {
        return ResponseEntity.ok(CustomApiResponse.of(HttpSuccessCode.USER_SEARCH
            , userService.findbyUserId(userId)));
    }

    @Operation(summary = "회원 검색", description = "회원 검색 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 검색 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    @GetMapping("/search")
    public ResponseEntity<CustomApiResponse<Page<UserSearchResponseDto>>> searchUser(
        UserSearchRequestDto Dto, Pageable pageable) {

        return ResponseEntity.ok(CustomApiResponse.of(HttpSuccessCode.USER_SEARCH
            , userService.search(Dto, pageable)));
    }

    @Operation(summary = "회원 수정", description = "회원 수정 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원 수정 성공"),
    })
    @PreAuthorize("isAuthenticated()")
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PutMapping
    public ResponseEntity<CustomApiResponse<UpdateUserResponseDto>> update(
        @Valid @RequestBody UpdateUserRequestDto requestDto,
        @CurrentLoginUserId Long currentUserId) {

        return ResponseEntity.ok(CustomApiResponse.of(HttpSuccessCode.USER_UPDATE
            , userService.updateUserInfo(requestDto, currentUserId)));
    }

    @Operation(summary = "회원 삭제", description = "회원 삭제 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "회원 삭제 성공"),
    })
    @ApiErrorCodeAnnotationList({ApiErrorCode.INVALID_REQUEST, ApiErrorCode.UNAUTHORIZED})
    @DeleteMapping("/{userId}")
    public ResponseEntity<CustomApiResponse<URI>> delete(@PathVariable Long userId
        , @AuthenticationPrincipal UserDetailsImpl userDetail) {

        userService.deleteUser(userId, userDetail);

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/users/login")
            .build()
            .toUri();

        return ResponseEntity.ok(CustomApiResponse.of(HttpSuccessCode.USER_DELETE, uri));
    }

}

package com.toyland.user.apllication.usecase;

import com.toyland.common.IntegrationTestSupport;
import com.toyland.global.config.security.UserDetailsImpl;
import com.toyland.user.application.UserService;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import com.toyland.user.presentation.dto.SignupRequestDto;
import com.toyland.user.presentation.dto.UpdateUserRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    UserDetailsImpl userDetails;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회원 정보 저장")
    @Test
    void createUser() {
        // when
        SignupRequestDto signupRequestDto = SignupRequestDto
                .builder()
                .username("testuser")
                .password("password123")
                .role(UserRoleEnum.MASTER)
                .build();

        userService.signup(signupRequestDto);

        // then
        List<User> all = userRepository.findAll();
        assertThat(all).hasSize(1)
                .extracting("username", "role")
                .containsExactlyInAnyOrder(
                        tuple("testuser", UserRoleEnum.MASTER)
                );

        User savedUser = all.get(0);
        assertThat(passwordEncoder.matches("password123", savedUser.getPassword())).isTrue();
    }

    @DisplayName("회원 정보 수정")
    @Test
    void updateUser() {
        //given
        userDetails = new UserDetailsImpl(
                new User(100L,
                        "master",
                        passwordEncoder.encode("password123"),
                        UserRoleEnum.MASTER));

        userRepository.save(new User("testuser",
                passwordEncoder.encode("password123"),
                UserRoleEnum.CUSTOMER));

        // when
        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto
                .builder()
                .username("updateuser")
                .password("password123")
                .role(UserRoleEnum.OWNER)
                .build();

        userService.updateUserInfo(updateUserRequestDto, 1L, userDetails);
        // then
        User updatedUser = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));

        assertEquals("updateuser", updatedUser.getUsername());
        assertTrue(passwordEncoder.matches("password123", updatedUser.getPassword()));
        assertEquals(UserRoleEnum.CUSTOMER, updatedUser.getRole());
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteUser() {
        // given
        userDetails = new UserDetailsImpl(
                new User(100L,
                        "master",
                        passwordEncoder.encode("password123"),
                        UserRoleEnum.MASTER));

        User testUser = new User("testuser",
                passwordEncoder.encode("password123"),
                UserRoleEnum.CUSTOMER);
        userRepository.save(testUser);

        // when
        userService.deleteUser(testUser.getId(), userDetails);

        // then
        User deletedUser = userRepository.findById(testUser.getId()).orElse(null);
        assertNotNull(deletedUser);
        assertNotNull(deletedUser.getDeletedAt());
        assertEquals(userDetails.getUser().getId(), deletedUser.getDeletedBy());
    }
}
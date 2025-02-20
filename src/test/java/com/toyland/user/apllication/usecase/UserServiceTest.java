package com.toyland.user.apllication.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.toyland.address.model.entity.Address;
import com.toyland.common.IntegrationTestSupport;
import com.toyland.global.config.security.UserDetailsImpl;
import com.toyland.order.model.Order;
import com.toyland.order.model.OrderStatus;
import com.toyland.order.model.OrderType;
import com.toyland.order.model.PaymentType;
import com.toyland.region.model.entity.Region;
import com.toyland.store.model.entity.Store;
import com.toyland.user.application.UserService;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import com.toyland.user.presentation.dto.SignupRequestDto;
import com.toyland.user.presentation.dto.UpdateUserRequestDto;
import com.toyland.user.presentation.dto.UserDto;
import com.toyland.user.presentation.dto.UserSearchRequestDto;
import com.toyland.user.presentation.dto.UserSearchResponseDto;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    UserDetailsImpl userDetails;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원 정보 저장")
    @Test
    void createUser() {
        // when
        SignupRequestDto signupRequestDto = SignupRequestDto
            .builder()
            .username("testuser")
            .password("Password123!")
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
        assertThat(passwordEncoder.matches("Password123!", savedUser.getPassword())).isTrue();
    }

    @DisplayName("회원 정보 수정")
    @Test
    @Transactional
    void updateUser() {
        // given
        User savedUser = userRepository.save(new User("master",
                passwordEncoder.encode("Password123!"),
                UserRoleEnum.MASTER));

        // when
        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto
                .builder()
                .username("updateuser")
                .password("Password123!") // 새로운 비밀번호
                .build();

        userService.updateUserInfo(updateUserRequestDto, savedUser.getId());

        // then
        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        assertEquals("updateuser", updatedUser.getUsername());
        assertTrue(passwordEncoder.matches("Password123!", updatedUser.getPassword()));
    }


    @DisplayName("회원 탈퇴")
    @Test
    @Transactional
    void deleteUser() {
        // given
        userDetails = new UserDetailsImpl(
                    UserDto.of(100L,
                        "master",
                        passwordEncoder.encode("Password123!"),
                        UserRoleEnum.MASTER));

        User testUser = new User("testuser",
            passwordEncoder.encode("Password123!"),
            UserRoleEnum.CUSTOMER);
        userRepository.save(testUser);

        // when
        userService.deleteUser(testUser.getId(), userDetails);

        // then
        User deletedUser = userRepository.findById(testUser.getId()).orElse(null);
        assertNotNull(deletedUser);
        assertNotNull(deletedUser.getDeletedAt());
        assertEquals(userDetails.getId(), deletedUser.getDeletedBy());
    }

    @DisplayName("회원 조회")
    @Test
    @Transactional
    void searchUser() {
        // given
        userDetails = new UserDetailsImpl(
                UserDto.of(100L,
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
        assertEquals(userDetails.getId(), deletedUser.getDeletedBy());
    }

    @DisplayName("회원 조회 (동적쿼리)")
    @Test
    @Transactional
    void dynamicSearch_User() {
        // given
        //주문생성
        Order order = Order.builder().orderStatus(OrderStatus.ORDER_CANCELED)
                .paymentType(PaymentType.CASH)
                .orderType(OrderType.DELIVERY)
                .build();
        //상점생성
        Store store = createStore("상점", "내용", "주소");
        //주소생성
        Address address = createAddress("천안");

        List<User> list = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            //유저생성
            list.add(userRepository.save(createMaster("master"+i)));
            order.joinUser(list.get(i));
            address.joinUser(list.get(i));
        }

        //검색 조건1
        UserSearchRequestDto case1 = new UserSearchRequestDto("master",UserRoleEnum.MASTER);

        //페이징 조건1
        Pageable pageable1 = PageRequest.of(0, 3, Sort.by("createdAt").ascending());
        //페이징 조건2
        Pageable pageable2 = PageRequest.of(0, 3, Sort.by("username").descending());


        Page<UserSearchResponseDto> search = userService.search(case1, pageable1);

        Page<UserSearchResponseDto> search2 = userService.search(case1, pageable2);


        assertThat(search).isNotEmpty();
        assertThat(search.getTotalElements()).isEqualTo(9);
        assertThat(search.getTotalPages()).isEqualTo(3);
        assertThat(search.getContent().get(0).username()).isEqualTo("master0");
        assertThat(search.getContent().get(0).role()).isEqualTo(UserRoleEnum.MASTER);
        assertThat(search.getContent().get(2).username()).isEqualTo("master2");

        // then
        assertThat(search2).isNotEmpty();
        assertThat(search2.getTotalElements()).isEqualTo(9);
        assertThat(search2.getTotalPages()).isEqualTo(3);
        assertThat(search2.getContent().get(0).username()).isEqualTo("master8");
        assertThat(search2.getContent().get(0).role()).isEqualTo(UserRoleEnum.MASTER);
        assertThat(search2.getContent().get(2).username()).isEqualTo("master6");
    }

    private User createMaster(String username) {
        return new User(username, "Password123!", UserRoleEnum.MASTER);
    }

    private Address createAddress(String name) {
        Region region = Region.builder().regionName("서울").build();
        return Address.builder()
            .addressName("주소이름")
            .region(region).build();
    }

    private Store createStore(String name){return createStore(name, "설명", "주소");}

    private Store createStore(String name, String content, String address) {
        return Store.builder()
                .name(name)
                .content(content)
                .address(address)
                .build();
    }

}
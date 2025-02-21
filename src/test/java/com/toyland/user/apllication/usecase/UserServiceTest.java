package com.toyland.user.apllication.usecase;

import com.toyland.address.model.entity.Address;
import com.toyland.common.IntegrationTestSupport;
import com.toyland.global.config.security.UserDetailsImpl;
import com.toyland.order.model.Order;
import com.toyland.order.model.OrderStatus;
import com.toyland.order.model.OrderType;
import com.toyland.order.model.PaymentType;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.store.model.entity.Store;
import com.toyland.user.application.UserService;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import com.toyland.user.presentation.dto.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
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

    @Autowired
    private RegionRepository regionRepository;

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

        UpdateUserResponseDto updateUserResponseDto = userService.updateUserInfo(updateUserRequestDto, savedUser.getId());

        // then
        assertEquals("updateuser", updateUserResponseDto.getUsername());
        assertTrue(passwordEncoder.matches("Password123!", updateUserResponseDto.getPassword()));
    }


    @DisplayName("회원 조회")
    @Test
    void searchUser() {
        // given
        User testUser = new User("testuser",
            passwordEncoder.encode("Password123!"),
            UserRoleEnum.CUSTOMER);
        userRepository.save(testUser);

        // when
        UserResponseDto userResponseDto = userService.findbyUserId(testUser.getId());

        // then
        assertThat(userResponseDto)
                .extracting("username","password", "role")
                .contains(testUser.getUsername(), testUser.getPassword(), testUser.getRole().toString());
    }

    @DisplayName("회원 탈퇴")
    @Test
    @Transactional
    void deleteUser() {
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
        Region region = Region.builder().regionName("서울").build();
        Region region1 = regionRepository.save(region);

        //주소생성
        Address address = createAddress("천안", region);

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

    private Address createAddress(String name, Region region) {
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
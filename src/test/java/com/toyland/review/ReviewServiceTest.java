package com.toyland.review;


import com.toyland.address.model.entity.Address;
import com.toyland.address.model.repository.AddressRepository;
import com.toyland.common.IntegrationTestSupport;
import com.toyland.order.model.Order;
import com.toyland.order.model.OrderStatus;
import com.toyland.order.model.OrderType;
import com.toyland.order.model.PaymentType;
import com.toyland.order.model.repository.OrderRepository;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.review.application.facade.ReviewFacade;
import com.toyland.review.model.repository.ReviewRepository;
import com.toyland.review.presentation.dto.ReviewRequestDto;
import com.toyland.review.presentation.dto.ReviewResponseDto;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class ReviewServiceTest extends IntegrationTestSupport {

  @Autowired
  private ReviewFacade reviewFacade;
  @Autowired
  private ReviewRepository reviewRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private RegionRepository regionRepository;
  @Autowired
  private AddressRepository addressRepository;

  @Test
  void calculateAvg() throws Exception {

    Double expectedAvg = 4.0;

    User user = userRepository.save(new User("test", "1234", UserRoleEnum.MASTER));
    User customer = userRepository.save(new User("customer", "1234", UserRoleEnum.CUSTOMER));

    //지역 생성
    Region region = regionRepository.save(new Region("서울"));

    //주소 생성
    Address address = addressRepository.save(createdAddress("영등포구", user, region)) ;


    Order order = orderRepository.save(
        new Order(customer, address, "300동 10호", PaymentType.CARD, OrderType.ONLINE_DELIVERY, OrderStatus.ORDER_CANCELED, "안맵게 해주세요."));

    Order order2 = orderRepository.save(
        new Order(customer, address, "302동 22호", PaymentType.CARD, OrderType.ONLINE_TAKEOUT, OrderStatus.ORDER_CANCELED, "맵게 해주세요."));


    Store store = storeRepository.save(
        Store.builder()
            .address("경기도 성남시 분당구 가로 1")
            .content("굽네치킨입니다.2")
            .name("굽네치킨")
            .region(region)  // UUID 대신 객체를 직접 전달
            .owner(user)  // UUID 대신 객체를 직접 전달
            .build());

    ReviewResponseDto responseDto = reviewFacade.createReview(
        new ReviewRequestDto(order.getId().toString(), store.getId().toString(), "good", 3));

    ReviewResponseDto responseDto2 = reviewFacade.createReview(
        new ReviewRequestDto(order2.getId().toString(), store.getId().toString(), "good2", 5));

    Store updatedStore = storeRepository.findById(store.getId()).orElse(null);

    assertThat(updatedStore.getAvgRating())
        .isEqualTo(expectedAvg);


  }


    private Address createdAddress(String addressName, User user, Region region) {
        return Address.builder()
                .addressName(addressName)
                .user(user)
                .region(region)
                .build();
    }

}

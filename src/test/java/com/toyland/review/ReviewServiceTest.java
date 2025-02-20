package com.toyland.review;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.toyland.common.IntegrationTestSupport;
import com.toyland.order.model.Order;
import com.toyland.order.model.OrderStatus;
import com.toyland.order.model.OrderType;
import com.toyland.order.model.PaymentType;
import com.toyland.order.model.repository.OrderRepository;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.review.application.usecase.ReviewService;
import com.toyland.review.model.Review;
import com.toyland.review.model.repository.ReviewRepository;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class ReviewServiceTest extends IntegrationTestSupport {

  @Autowired
  private ReviewService reviewService;
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

  @Test
  void calculateAvg() throws Exception {

    Double expectedAvg = 4.0;

    User user = userRepository.save(new User("test", "1234", UserRoleEnum.MASTER));

    Order order = orderRepository.save(
        new Order(user, PaymentType.CARD, OrderType.DELIVERY, OrderStatus.ORDER_CANCELED));

    Order order2 = orderRepository.save(
        new Order(user, PaymentType.CARD, OrderType.DELIVERY, OrderStatus.ORDER_CANCELED));

    Region region = regionRepository.save(new Region("서울"));

    Store store = storeRepository.save(
        Store.builder()
            .address("경기도 성남시 분당구 가로 1")
            .content("굽네치킨입니다.2")
            .name("굽네치킨")
            .region(region)  // UUID 대신 객체를 직접 전달
            .owner(user)  // UUID 대신 객체를 직접 전달
            .build());

    Review review1 = reviewRepository.save(new Review("good", 3, store, order));
    Review review2 = reviewRepository.save(new Review("good2", 5, store, order2));

    // store.addReview(review1);
    // store.addReview(review2);

    storeRepository.save(store);
    reviewRepository.save(review1);
    reviewRepository.save(review2);

    Double avg = reviewService.getAvgRate(store.getId().toString());

    assertThat(avg)
        .isNotNull()
        .isEqualTo(expectedAvg);

  }

}

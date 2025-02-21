package com.toyland.ai;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.toyland.ai.application.usecase.QnaService;
import com.toyland.ai.model.Qna;
import com.toyland.ai.model.repository.QnaRepository;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import com.toyland.common.IntegrationTestSupport;
import com.toyland.global.config.security.jwt.JwtUtil;
import com.toyland.region.model.entity.Region;
import com.toyland.region.model.repository.RegionRepository;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import com.toyland.user.model.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@AutoConfigureMockMvc
public class QnaControllerTest extends IntegrationTestSupport {


  @Autowired
  private QnaService qnaService;

  @Autowired
  private QnaRepository qnaRepository;

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private RegionRepository regionRepository;


  @DisplayName("Qna를 수정한다")
  @Test
  void updateAiQna() throws Exception {

    User owner = userRepository.save(
        new User("new_user", "password123", UserRoleEnum.MASTER)
    );

    String token = jwtUtil.createToken("new_user", UserRoleEnum.MASTER);

    Region region = regionRepository.save(new Region("서울"));

    Store store = storeRepository.save(
        Store.builder()
            .address("경기도 성남시 분당구 가로 1")
            .content("굽네치킨입니다.")
            .name("굽네치킨")
            .region(region)  // UUID 대신 객체를 직접 전달
            .owner(owner)  // UUID 대신 객체를 직접 전달
            .build()
    );

    Store testStore = storeRepository.save(store);

    Qna qna = qnaRepository.save(new Qna("Old Question", "Old Answer", testStore));
    UUID qnaId = qna.getAiId();

    QnaRequestDto requestDto = new QnaRequestDto(qna.getAiId(), "Updated Question",
        "Updated Answer", store.getId().toString());

    // When
    QnaResponseDto responseDto = qnaService.updateQna(requestDto, qnaId);

    // Then
    assertThat(responseDto)
        .isNotNull()
        .extracting(QnaResponseDto::getQuestion, QnaResponseDto::getAnswer)
        .containsExactly("Updated Question", "Updated Answer");
    // content 필드 확인
  }


}
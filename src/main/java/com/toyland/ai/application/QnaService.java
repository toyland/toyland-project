package com.toyland.ai.application;

import com.toyland.ai.model.AiComp;
import com.toyland.ai.model.Qna;
import com.toyland.ai.model.repository.QnaRepository;
import com.toyland.ai.presentation.OpenApiFeignClient;
import com.toyland.ai.presentation.dto.AiRequestDto;
import com.toyland.ai.presentation.dto.AiResponseDto;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import com.toyland.global.exception.type.ApiErrorCode;
import com.toyland.user.model.User;
import com.toyland.user.model.UserRoleEnum;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QnaService {

  private final QnaRepository qnaRepository;
  private final AiComp aiComp;
  private final OpenApiFeignClient openApiFeignClient;


  public Qna createQna(QnaRequestDto qnaRequestDto) {
    AiRequestDto request = new AiRequestDto(aiComp.getModel(), qnaRequestDto.getQuestion());
    AiResponseDto response = openApiFeignClient.getAnswer(request);
    String answer = response.getChoices().get(0).getMessage().getContent();
    qnaRequestDto.setAnswer(answer);
    Qna qna = Qna.of(qnaRequestDto);
    return qnaRepository.save(qna);
  }


  public QnaResponseDto getQna(UUID qnaId) {
    return qnaRepository.findByAiId(qnaId).orElseThrow(
        () -> new IllegalArgumentException("존재하지 않는 qna입니다. qna id를 확인해주세요."));
  }


  public Page<QnaResponseDto> getQnaList(User user, int i, int size, String sortBy, boolean isAsc,
      UUID storeId) {
    //페이징 처리
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(i, size, sort);

    UserRoleEnum userRoleEnum = user.getRole();
    if (userRoleEnum == UserRoleEnum.CUSTOMER) {
      throw ApiErrorCode.FORBIDDEN.toException();
    }

    Long userId = user.getId();
    Page<Qna> qnaList;

    // 사용자 권한 가져와서 ADMIN 이면 전체 조회, OWNER 면 본인이 추가한 부분 조회
    if (userRoleEnum == UserRoleEnum.OWNER) {
      // 사용자 권한이 OWNER 일 경우
      qnaList = qnaRepository.findAllByOwner(userId, pageable, storeId);
    } else {
      //Manager 혹은 ADMIN 일 경우
      qnaList = qnaRepository.findAllByStoreId(pageable, storeId);
    }

    return qnaList.map(QnaResponseDto::new);

  }


}

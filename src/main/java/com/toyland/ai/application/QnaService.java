package com.toyland.ai.application;

import com.toyland.ai.model.AiComp;
import com.toyland.ai.model.Qna;
import com.toyland.ai.model.repository.QnaRepository;
import com.toyland.ai.model.repository.QnaRepositoryCustom;
import com.toyland.ai.presentation.OpenApiFeignClient;
import com.toyland.ai.presentation.dto.AiRequestDto;
import com.toyland.ai.presentation.dto.AiResponseDto;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.ProductErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
  private final QnaRepositoryCustom qnaRepositoryCustom;

  @Autowired
  public QnaService(
      QnaRepository qnaRepository,
      @Qualifier("qnaRepositoryCustomImpl") QnaRepositoryCustom qnaRepositoryCustom,
      AiComp aiComp,
      OpenApiFeignClient openApiFeignClient) {
    this.qnaRepository = qnaRepository;
    this.qnaRepositoryCustom = qnaRepositoryCustom;
    this.aiComp = aiComp;
    this.openApiFeignClient = openApiFeignClient;
  }

  public Qna createQna(QnaRequestDto qnaRequestDto) {
    AiRequestDto request = new AiRequestDto(aiComp.getModel(), qnaRequestDto.getQuestion());
    AiResponseDto response = openApiFeignClient.getAnswer(request);
    String answer = response.getChoices().get(0).getMessage().getContent();
    qnaRequestDto.setAnswer(answer);
    Qna qna = Qna.from(qnaRequestDto);
    return qnaRepository.save(qna);
  }


  public Qna getQna(UUID qnaId) {
    return qnaRepository.findById(qnaId).orElseThrow(
        () -> CustomException.from(ProductErrorCode.NOT_FOUND)
    );
  }


  public Page<QnaResponseDto> getQnaList(int i, int size, String sortBy, boolean isAsc,
      UUID storeId) {
    //페이징 처리
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(i, size, sort);

    Page<Qna> qnaPage = qnaRepositoryCustom.searchQnas(storeId, pageable);

    Page<QnaResponseDto> qnaList = qnaPage.map(QnaResponseDto::from);

    return qnaList;
  }


}

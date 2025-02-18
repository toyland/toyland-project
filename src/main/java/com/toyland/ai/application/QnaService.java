package com.toyland.ai.application;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.by;

import com.toyland.ai.model.AiComp;
import com.toyland.ai.model.Qna;
import com.toyland.ai.model.repository.QnaRepository;
import com.toyland.ai.presentation.OpenApiFeignClient;
import com.toyland.ai.presentation.dto.AiRequestDto;
import com.toyland.ai.presentation.dto.AiResponseDto;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import com.toyland.global.exception.CustomException;
import com.toyland.global.exception.type.domain.ProductErrorCode;
import com.toyland.store.model.entity.Store;
import com.toyland.store.model.repository.StoreRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QnaService {


  private final QnaRepository qnaRepository;
  private final AiComp aiComp;
  private final OpenApiFeignClient openApiFeignClient;
  private final QnaRepositoryCustom qnaRepositoryCustom;
  private final StoreRepository storeRepository;

  @Autowired
  public QnaService(
      QnaRepository qnaRepository,
      @Qualifier("qnaRepositoryCustomImpl") QnaRepositoryCustom qnaRepositoryCustom,
      AiComp aiComp,
      OpenApiFeignClient openApiFeignClient, StoreRepository storeRepository) {
    this.qnaRepository = qnaRepository;
    this.qnaRepositoryCustom = qnaRepositoryCustom;
    this.aiComp = aiComp;
    this.openApiFeignClient = openApiFeignClient;
    this.storeRepository = storeRepository;
  }

  @Transactional
  public QnaResponseDto createQna(QnaRequestDto qnaRequestDto) {
    String answer = fetchAiAnswer(qnaRequestDto.getQuestion());
    qnaRequestDto.setAnswer(answer);
    Store store = findStoreById(qnaRequestDto.getStoreId());
    Qna result = saveQna(qnaRequestDto, store);
    return QnaResponseDto.of(result);
  }

  /**
   * AI 응답 가져오기
   */
  private String fetchAiAnswer(String question) {
    AiRequestDto request = new AiRequestDto(aiComp.getModel(), question);
    AiResponseDto response = openApiFeignClient.getAnswer(request);
    return response.getChoices().get(0).getMessage().getContent();
  }

  /**
   * Store 찾기 기능 분리
   */
  private Store findStoreById(String storeId) {
    UUID uuid = UUID.fromString(storeId);
    return storeRepository.findById(uuid)
        .orElseThrow(() -> CustomException.from(ProductErrorCode.NOT_FOUND));
  }

  /**
   * Qna 저장 기능 분리
   */
  private Qna saveQna(QnaRequestDto qnaRequestDto, Store store) {
    Qna qna = Qna.from(qnaRequestDto, store);
    return qnaRepository.save(qna);

  }


  public QnaResponseDto getQna(UUID qnaId) {
    Qna qna = qnaRepository.findById(qnaId).orElseThrow(
        () -> CustomException.from(ProductErrorCode.NOT_FOUND)
    );
    QnaResponseDto qnaResponseDto = new QnaResponseDto(qna);
    return qnaResponseDto;
  }


  public Page<QnaResponseDto> getQnaList(Pageable pageable, UUID storeId) {
    Page<Qna> qnaPage = qnaRepositoryCustom.searchQnas(storeId, pageable);
    Page<QnaResponseDto> qnaList = qnaPage.map(QnaResponseDto::of);
    return qnaList;
  }

  @Transactional
  public QnaResponseDto updateQna(QnaRequestDto request, UUID qnaId) {
    Qna qna = qnaRepository.findById(qnaId).orElseThrow(
        () -> CustomException.from(ProductErrorCode.NOT_FOUND)
    );
    qna.update(request);
    return QnaResponseDto.of(qna);

  }


  @Transactional
  public void delete(UUID qnaId, Long id) {
    Qna qna = qnaRepository.findById(qnaId).orElseThrow(
        () -> CustomException.from(ProductErrorCode.NOT_FOUND)
    );
    qna.addDeletedField(id);

  }
}

package com.toyland.ai.application.facade;


import com.toyland.ai.application.usecase.QnaService;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class QnaFacadeImpl implements QnaFacade {

  private final QnaService qnaService;

  @Override
  public QnaResponseDto createQna(QnaRequestDto request) {
    return qnaService.createQna(request);
  }

  @Override
  public QnaResponseDto getQna(UUID qnaId) {
    return qnaService.getQna(qnaId);
  }

  @Override
  public Page<QnaResponseDto> getQnaList(Pageable pageable, UUID storeId) {
    return qnaService.getQnaList(pageable, storeId);
  }

  @Override
  public QnaResponseDto updateQna(QnaRequestDto request, UUID qnaId) {
    return qnaService.updateQna(request, qnaId);
  }

  @Override
  public void delete(UUID qnaId, Long loginUserId) {
    qnaService.delete(qnaId, loginUserId);
  }
}

package com.toyland.ai.application.facade;

import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaFacade {

  QnaResponseDto createQna(QnaRequestDto request);

  QnaResponseDto getQna(UUID qnaId);

  Page<QnaResponseDto> getQnaList(Pageable pageable, UUID storeId);

  QnaResponseDto updateQna(QnaRequestDto request, UUID qnaId);

  void delete(UUID qnaId, Long loginUserId);
}

package com.toyland.ai.application.usecase;

import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaService {

  QnaResponseDto createQna(QnaRequestDto qnaRequestDto);

  QnaResponseDto getQna(UUID qnaId);

  Page<QnaResponseDto> getQnaList(Pageable pageable, UUID storeId);

  QnaResponseDto updateQna(QnaRequestDto qnaRequestDto, UUID qnaId);

  void delete(UUID qnaId, Long userId);

}
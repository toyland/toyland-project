package com.toyland.ai.model.repository;

import com.toyland.ai.model.Qna;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface QnaRepository {

  Qna save(Qna qna);

  Optional<QnaResponseDto> findByAiId(UUID qnaId);

  @Query(value = "SELECT a.question, a.answer FROM p_aiqna a " +
      "JOIN p_store b ON a.storeId = b.storeId " +
      "WHERE b.userId = :userId AND a.storeId = :storeId",
      nativeQuery = true)
  Page<Qna> findAllByOwner(Long userId, Pageable pageable, UUID storeId);

  Page<Qna> findAllByStoreId(Pageable pageable, UUID storeId);
}

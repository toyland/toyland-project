package com.toyland.ai.model.repository;

import com.toyland.ai.model.Qna;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface QnaRepository {

  Qna save(Qna qna);

  Optional<Qna> findByAiId(UUID qnaId);

  @Query(value = "SELECT a.question, a.answer FROM p_aiqna a " +
      "JOIN p_store b ON a.store_Id = b.store_Id " +
      "WHERE b.user_Id = :userId AND a.store_Id = CAST(:storeId AS UUID)",
      nativeQuery = true)
  Page<Qna> findAllByOwner(Long userId, UUID storeId, Pageable pageable);


  Page<Qna> findAllByStoreId(UUID storeId, Pageable pageable);
}

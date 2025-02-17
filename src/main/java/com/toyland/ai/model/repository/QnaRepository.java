package com.toyland.ai.model.repository;

import com.toyland.ai.model.Qna;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, UUID> {

  Qna save(Qna qna);

  Optional<Qna> findByAiId(UUID qnaId);

}

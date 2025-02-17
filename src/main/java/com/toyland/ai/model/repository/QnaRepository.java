package com.toyland.ai.model.repository;

import com.toyland.ai.model.Qna;
import java.util.Optional;
import java.util.UUID;

public interface QnaRepository {

    Qna save(Qna qna);

    Optional<Qna> findById(UUID qnaId);

//    Page<Qna> searchQna(UUID storeId, Pageable pageable);

}

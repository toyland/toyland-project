package com.toyland.ai.model.repository;

import com.toyland.ai.model.Qna;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaRepositoryCustom {

  Page<Qna> searchQnas(UUID storeId, Pageable pageable);
}

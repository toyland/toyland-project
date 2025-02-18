package com.toyland.ai.infrastructure;

import com.toyland.ai.model.Qna;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 18.
 */
public interface QnaRepositoryCustom {

    Page<Qna> searchQna(UUID storeId, Pageable pageable);
}

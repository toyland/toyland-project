package com.toyland.ai.infrastructure;

import com.toyland.ai.model.Qna;
import com.toyland.ai.model.repository.QnaRepository;
import com.toyland.ai.model.repository.QnaRepositoryCustom;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAIRepository extends QnaRepository, JpaRepository<Qna, UUID>,
    QnaRepositoryCustom {

}

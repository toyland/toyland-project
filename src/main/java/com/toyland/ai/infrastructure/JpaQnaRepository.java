package com.toyland.ai.infrastructure;

import com.toyland.ai.infrastructure.custom.QnaRepositoryCustom;
import com.toyland.ai.model.Qna;
import com.toyland.ai.model.repository.QnaRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaQnaRepository extends QnaRepository, JpaRepository<Qna, UUID>,
    QnaRepositoryCustom {

}

package com.toyland.ai.infrastructure;

import com.toyland.ai.model.Qna;
import com.toyland.ai.model.repository.QnaRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaQnaRepository extends QnaRepository, JpaRepository<Qna, UUID> {

}

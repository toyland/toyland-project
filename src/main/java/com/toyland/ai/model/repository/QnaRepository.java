package com.toyland.ai.model.repository;

import com.toyland.ai.model.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QnaRepository extends JpaRepository<Qna, UUID> {


}

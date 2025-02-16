package com.toyland.ai.application;

import com.toyland.StoreRepository;
import com.toyland.ai.model.Qna;
import com.toyland.ai.model.repository.QnaRepository;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QnaService {

    private final QnaRepository qnaRepository;

    public QnaService(QnaRepository qnaRepository) {
        this.qnaRepository = qnaRepository;
    }

    @Autowired
    private StoreRepository storeRepository;

    public Qna createQna(QnaRequestDto qnaRequestDto) {
        String id = qnaRequestDto.getStore_id();

        Qna qna = new Qna();
        qna.setQuestion(qnaRequestDto.getQuestion());
        qna.setAnswer(qnaRequestDto.getAnswer());
        qna.setStoreId(UUID.fromString(id));

        return qnaRepository.save(qna);

    }



}

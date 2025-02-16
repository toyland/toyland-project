package com.toyland.ai.application;

import com.toyland.ai.model.AiComp;
import com.toyland.ai.model.Qna;
import com.toyland.ai.model.repository.QnaRepository;
import com.toyland.ai.presentation.dto.AiRequestDto;
import com.toyland.ai.presentation.dto.AiResponseDto;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;
    private final AiComp aiComp;
    private final com.toyland.ai.presentation.OpenApiFeignClient OpenApiFeignClient;


    public Qna createQna(QnaRequestDto qnaRequestDto) {
        AiRequestDto request = new AiRequestDto(aiComp.getModel(), qnaRequestDto.getQuestion());
        AiResponseDto response = OpenApiFeignClient.getAnswer(request);
        String answer = response.getChoices().get(0).getMessage().getContent();
        qnaRequestDto.setAnswer(answer);
        Qna qna = Qna.of(qnaRequestDto);
        return qnaRepository.save(qna);
    }



}

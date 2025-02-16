package com.toyland.ai.presentation;


import com.toyland.ai.application.QnaService;
import com.toyland.ai.model.Qna;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qna")
public class QnaController {

   private final QnaService qnaService;

    /**
     * AI에게 물어본 질문과 그에대한 답변을 저장합니다.
     * @param request 음식점의 질문
     * @return 응답 포함한 객체
     */
    @PostMapping("/")
    public ResponseEntity<Qna> createAiQna(@RequestBody QnaRequestDto request) {
        Qna qna =  qnaService.createQna(request);
        return ResponseEntity.ok(qna);
    }


}

package com.toyland.ai.presentation;


import com.toyland.ai.application.QnaService;
import com.toyland.ai.model.AiComp;
import com.toyland.ai.model.Qna;
import com.toyland.ai.presentation.dto.AiRequestDto;
import com.toyland.ai.presentation.dto.AiResponseDto;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class QnaController {

   private final AiComp aiComp;
   private final OpenApiFeignClient OpenApiFeignClient;
   private final QnaService qnaService;


    /**
     * OpenAI로 질문을 보내고 답변을 받습니다.
     * @param qna 사용자 질문
     * @return OpenAI 응답
     */
    @GetMapping("/ai_qna")
    public ResponseEntity<String> getAnswer(@RequestParam String qna) {
        AiRequestDto request = new AiRequestDto(aiComp.getModel(),qna);
        AiResponseDto response = OpenApiFeignClient.getAnswer(request);
        String answer = response.getChoices().get(0).getMessage().getContent();
        return ResponseEntity.ok(answer);
    }

    /**
     * AI로 주고받은 질문과 답변을 저장합니다.
     * @param request (음식점id,질문,응답)
     * @return
     */
    @PostMapping("/qna")
    public ResponseEntity<Qna> postAnswer(@RequestBody QnaRequestDto request) {
        Qna qna =  qnaService.createQna(request);
        return ResponseEntity.ok(qna);
    }


}

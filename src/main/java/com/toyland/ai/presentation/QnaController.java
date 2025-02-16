package com.toyland.ai.presentation;


import com.toyland.ai.application.QnaService;
import com.toyland.ai.model.Qna;
import com.toyland.ai.presentation.dto.AiRequestDto;
import com.toyland.ai.presentation.dto.AiResponseDto;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1")
public class QnaController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate restTemplate;

    private final QnaService qnaService;

    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    /**
     * OpenAI로 질문을 보내고 답변을 받습니다.
     * @param qna 사용자 질문
     * @return OpenAI 응답
     */
    @GetMapping("/ai_qna")
    public ResponseEntity<String> getAnswer(@RequestParam String qna) {
        AiRequestDto request = new AiRequestDto(model, qna);
        AiResponseDto response = restTemplate.postForObject(apiURL, request, AiResponseDto.class);
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

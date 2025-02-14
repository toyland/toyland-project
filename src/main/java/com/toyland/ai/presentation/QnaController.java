package com.toyland.ai.presentation;


import com.toyland.ai.presentation.dto.AiRequestDto;
import com.toyland.ai.presentation.dto.AiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class QnaController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/qna")
    public ResponseEntity getAnswer(@RequestParam String qna) {
        AiRequestDto request = new AiRequestDto(model, qna);
        AiResponseDto response = restTemplate.postForObject(apiURL, request, AiResponseDto.class);
        return ResponseEntity.ok(response.getChoices().get(0).getMessage().getContent());
    }


}

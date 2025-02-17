package com.toyland.ai.presentation;


import com.toyland.ai.application.QnaService;
import com.toyland.ai.model.Qna;
import com.toyland.ai.presentation.dto.PagedResponse;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import com.toyland.global.config.security.UserDetailsImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qna")
public class QnaController {

  private final QnaService qnaService;

  /**
   * AI에게 물어본 질문과 그에대한 답변을 저장합니다.
   *
   * @param request 음식점의 질문
   * @return qna 내용
   */
  @PostMapping("/")
  public ResponseEntity<Qna> createAiQna(@RequestBody QnaRequestDto request) {
    Qna qna = qnaService.createQna(request);
    return ResponseEntity.ok(qna);
  }

  /**
   * AI 질문과 답변을 한 건 가져온다.
   *
   * @param qnaId
   * @return 질문과 답변
   */
  @GetMapping("/{qnaId}")
  public ResponseEntity<Qna> getAiQna(@PathVariable UUID qnaId) {
    Qna qna = qnaService.getQna(qnaId);
    return ResponseEntity.ok(qna);
  }

  /**
   * 해당 유저의 음식점에서 질문한 질문과 답변을 페이징하여 가져온다.
   *
   * @param page
   * @param size
   * @param sortBy
   * @param isAsc
   * @param storeId
   * @param userDetails
   * @return 질문/답변 리스트
   */
  @GetMapping("/search")
  public ResponseEntity<PagedResponse<QnaResponseDto>> getAiQnaList(@RequestParam("page") int page,
      @RequestParam("size") int size,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("isAsc") boolean isAsc,
      @RequestParam UUID storeId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    Page<QnaResponseDto> qnaList = qnaService.getQnaList(userDetails.getUser(), page - 1, size,
        sortBy, isAsc, storeId);
    return ResponseEntity.ok(new PagedResponse<>(qnaList));
  }


}

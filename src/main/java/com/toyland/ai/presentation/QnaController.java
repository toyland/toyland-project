package com.toyland.ai.presentation;


import com.toyland.ai.application.QnaService;
import com.toyland.ai.presentation.dto.PagedResponse;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import com.toyland.global.config.security.UserDetailsImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
   * AI에게 물어본 질문과 그에대한 답변을 저장한한다.
   *
   * @param request 음식점의 질문
   * @return qna 내용
   */
  @PostMapping
  public ResponseEntity<QnaResponseDto> createAiQna(@RequestBody QnaRequestDto request) {
    QnaResponseDto qna = qnaService.createQna(request);
    return ResponseEntity.ok(qna);
  }

  /**
   * AI 질문과 답변을 한 건 가져온다.
   *
   * @param qnaId
   * @return 질문과 답변
   */
  @GetMapping("/{qnaId}")
  public ResponseEntity<QnaResponseDto> getAiQna(@PathVariable UUID qnaId) {
    QnaResponseDto qna = qnaService.getQna(qnaId);
    return ResponseEntity.ok(qna);
  }

  /**
   * 해당 유저의 음식점에서 질문한 질문과 답변을 페이징하여 가져온다.
   *
   * @param pageable
   * @param storeId
   * @return 질문/답변 리스트
   */
  @GetMapping("/search")
  public ResponseEntity<PagedResponse<QnaResponseDto>> getAiQnaList(Pageable pageable,
      @RequestParam UUID storeId) {
    Page<QnaResponseDto> qnaList = qnaService.getQnaList(pageable, storeId);
    return ResponseEntity.ok(new PagedResponse<>(qnaList));
  }


  /**
   * AI의 질문,답변을 수정한다.
   *
   * @param request
   * @param qnaId
   * @return 수정된 내용
   */
  @PutMapping("/{qnaId}")
  public ResponseEntity<QnaResponseDto> updateAiQna(@RequestBody QnaRequestDto request,
      @PathVariable UUID qnaId) {
    QnaResponseDto responseDto = qnaService.updateQna(request, qnaId);
    return ResponseEntity.ok(responseDto);
  }


  /**
   * AI의 질문,답변을 삭제한다.
   *
   * @param qnaId
   * @param userDetails
   */
  @DeleteMapping("/{qnaId}")
  public void deleteAiQna(@PathVariable UUID qnaId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    qnaService.delete(qnaId, userDetails.getUserId());
  }

}

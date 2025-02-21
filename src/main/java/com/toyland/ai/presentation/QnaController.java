package com.toyland.ai.presentation;


import com.toyland.ai.application.facade.QnaFacade;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qna")
public class QnaController {

  private final QnaFacade qnaFacade;


  /**
   * AI에게 물어본 질문과 그에대한 답변을 저장한한다.
   *
   * @param request 음식점의 질문
   * @return qna 내용
   */
  @PostMapping
  public ResponseEntity<QnaResponseDto> createAiQna(@RequestBody QnaRequestDto request) {
    QnaResponseDto qna = qnaFacade.createQna(request);
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
    QnaResponseDto qna = qnaFacade.getQna(qnaId);
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
  public ResponseEntity<Page<QnaResponseDto>> getAiQnaList(Pageable pageable,
      @RequestParam UUID storeId) {
    log.info("Received storeId: {}", storeId);
    Page<QnaResponseDto> qnaList = qnaFacade.getQnaList(pageable, storeId);
    return ResponseEntity.ok(qnaList);
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
    QnaResponseDto responseDto = qnaFacade.updateQna(request, qnaId);
    return ResponseEntity.ok(responseDto);
  }


  /**
   * AI의 질문,답변을 삭제한다.
   *
   * @param qnaId
   * @param loginUserId
   */
  @DeleteMapping("/{qnaId}")
  public void deleteAiQna(@PathVariable UUID qnaId,
      @CurrentLoginUserId Long loginUserId) {
    qnaFacade.delete(qnaId, loginUserId);

  }

}

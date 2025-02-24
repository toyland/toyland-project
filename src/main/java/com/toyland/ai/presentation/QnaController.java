package com.toyland.ai.presentation;


import com.toyland.ai.application.facade.QnaFacade;
import com.toyland.ai.presentation.dto.QnaRequestDto;
import com.toyland.ai.presentation.dto.QnaResponseDto;
import com.toyland.global.config.security.annotation.CurrentLoginUserId;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotation;
import com.toyland.global.config.swagger.annotation.ApiErrorCodeAnnotationList;
import com.toyland.global.config.swagger.response.CustomApiResponse;
import com.toyland.global.config.swagger.response.HttpSuccessCode;
import com.toyland.global.exception.type.ApiErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
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
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qna")
@Tag(name = "QnA", description = "QnA API")
public class QnaController {

    private final QnaFacade qnaFacade;


    /**
     * AI에게 물어본 질문과 그에대한 답변을 저장한한다.
     *
     * @param request 음식점의 질문
     * @return qna 내용
     */
    @Operation(summary = "QnA 등록", description = "QnA 등록 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "QnA 등록 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PostMapping
    public ResponseEntity<CustomApiResponse<QnaResponseDto>> createAiQna(
        @RequestBody QnaRequestDto request) {
        QnaResponseDto qna = qnaFacade.createQna(request);

        URI uri = UriComponentsBuilder.fromUriString("/api/v1/qna/{qnaId}")
            .buildAndExpand(qna.getQnaId())
            .toUri();

        return ResponseEntity
            .created(uri)
            .body(CustomApiResponse.of(HttpSuccessCode.QNA_CREATE, qna));
    }

    /**
     * AI 질문과 답변을 한 건 가져온다.
     *
     * @param qnaId
     * @return 질문과 답변
     */
    @Operation(summary = "QnA 조회", description = "QnA 조회 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "QnA 조회 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/{qnaId}")
    public ResponseEntity<CustomApiResponse<QnaResponseDto>> getAiQna(@PathVariable UUID qnaId) {
        QnaResponseDto qna = qnaFacade.getQna(qnaId);
        return ResponseEntity
            .ok(CustomApiResponse.of(HttpSuccessCode.QNA_FIND_ONE,
                qnaFacade.getQna(qna.getQnaId())));
    }

    /**
     * 해당 유저의 음식점에서 질문한 질문과 답변을 페이징하여 가져온다.
     *
     * @param pageable
     * @param storeId
     * @return 질문/답변 리스트
     */
    @Operation(summary = "QnA 검색", description = "QnA 검색 api 입니다." +
        " 예시 http://localhost:8080/api/v1/qna/search?page=0&size=10&sort=createdAt,asc&\n"
        + "    storeId=2e11ee82-82a9-41cc-ac52-3b532887d5e6")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "QnA 검색 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @GetMapping("/search")
    public ResponseEntity<CustomApiResponse<Page<QnaResponseDto>>> getAiQnaList(Pageable pageable,
        @RequestParam UUID storeId) {
        log.info("Received storeId: {}", storeId);
        Page<QnaResponseDto> qnaList = qnaFacade.getQnaList(pageable, storeId);
        return ResponseEntity
            .ok(CustomApiResponse.of(HttpSuccessCode.QNA_SEARCH, qnaList));
    }


    /**
     * AI의 질문,답변을 수정한다.
     *
     * @param request
     * @param qnaId
     * @return 수정된 내용
     */
    @Operation(summary = "QnA 수정", description = "QnA 수정 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "QnA 수정 성공"),
    })
    @ApiErrorCodeAnnotation(ApiErrorCode.INVALID_REQUEST)
    @PutMapping("/{qnaId}")
    public ResponseEntity<CustomApiResponse<QnaResponseDto>> updateAiQna(
        @RequestBody QnaRequestDto request,
        @PathVariable UUID qnaId) {
        QnaResponseDto responseDto = qnaFacade.updateQna(request, qnaId);
        return ResponseEntity.ok(CustomApiResponse.of(HttpSuccessCode.QNA_UPDATE, responseDto));
    }


    /**
     * AI의 질문,답변을 삭제한다.
     *
     * @param qnaId
     * @param loginUserId
     */
    @Operation(summary = "QnA 삭제", description = "QnA 삭제 api 입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "QnA 삭제 성공"),
    })
    @ApiErrorCodeAnnotationList({ApiErrorCode.INVALID_REQUEST, ApiErrorCode.UNAUTHORIZED})
    @DeleteMapping("/{qnaId}")
    public ResponseEntity<CustomApiResponse<Void>> deleteAiQna(@PathVariable UUID qnaId,
        @CurrentLoginUserId Long loginUserId) {
        qnaFacade.delete(qnaId, loginUserId);

        return ResponseEntity.noContent().build();
    }

}

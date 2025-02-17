package com.toyland.global.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : hanjihoon
 * @Date : 2025. 02. 16.
 */
@RestControllerAdvice
public class GlobalErrorHandler {

    /**
     * 직접 커스텀한 예외 핸들러
     *
     * @param e 커스텀한 Exception
     * @return ErrorResponse
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ResponseEntity
            .status(e.getHttpStatus())
            .body(new ErrorResponse(e.getCode(), e.getMessage(), e.getHttpStatus()));
    }

    // 잘못된 요청 파라미터 등의 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("INVALID_ARGUMENT", e.getMessage(),
                HttpStatus.BAD_REQUEST.value()));
    }

    //그 외 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    /**
     * 요청 받는 DTO 검증 에러 ExceptionHandler
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        List<Response.ErrorField<Object>> errorFields = e.getBindingResult().getFieldErrors()
            .stream()
            .map(fieldError -> new Response.ErrorField<>(
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage()))
            .toList();

        return ResponseEntity
            .badRequest()
            .body(new Response("INVALID_REQUEST_400", "입력값이 올바르지 않습니다.", errorFields));
    }


    /**
     * @PathVariable에서 Valid 검증 에러 ExceptionHandler
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response> handleConstraintViolationException(
        ConstraintViolationException e) {
        List<Response.ErrorField<Object>> errorFields = e.getConstraintViolations().stream()
            .map(violation -> new Response.ErrorField<>(
                violation.getInvalidValue(),
                violation.getMessage()))
            .toList();

        return ResponseEntity
            .badRequest()
            .body(new Response("INVALID_ARGUMENT_400", "요청값이 올바르지 않습니다.", errorFields));
    }


    public record Response(String code, String message, List<ErrorField<Object>> errors) {

        public record ErrorField<T>(T value, String message) {

        }

    }


}

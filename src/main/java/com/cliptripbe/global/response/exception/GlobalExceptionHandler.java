package com.cliptripbe.global.response.exception;

import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.type.ErrorType;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        log.warn(e.getMessage(), e);
        return ApiResponse.error(ErrorType.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ApiResponse<?> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn(e.getMessage(), e);
        return ApiResponse.error(ErrorType.ENTITY_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    protected ApiResponse<?> handleBusinessException(CustomException e) {
        log.error("CustomException", e);
        return ApiResponse.error(e.getErrorType());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .findFirst()
            .orElse("잘못된 요청입니다.");
        return ApiResponse.error(ErrorType.INVALID_REQUEST, errorMessage);
    }
}

package com.example.IdentifyUser.Exception;


import com.example.IdentifyUser.dto.reponse.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return  ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED_ACCESS;

        String detail = exception.getMessage();
        log.info(detail);
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        String detail = ex.getFieldError().getDefaultMessage();
        log.info(detail);
        String errorKey = ex.getFieldError().getDefaultMessage();
        Map<String, Object> attributes = null;
        try{
            errorCode = ErrorCode.valueOf(errorKey);

            var constrainViolation = ex.getBindingResult()
                    .getAllErrors().getFirst()
                    .unwrap(ConstraintViolation.class);

            attributes = constrainViolation.getConstraintDescriptor()
                    .getAttributes();

            log.info(attributes.toString());
            log.info(attributes.getClass().getName());

        }catch (IllegalArgumentException e){

        }

        return ResponseEntity
                .status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(Objects.nonNull(attributes) ?
                                mapMessage(errorCode.getMessage(), attributes)
                                : errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
        log.info(ex.getMessage());
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    private String mapMessage(String message, Map<String, Object> attributes){
        message = message.replace("{"+MIN_ATTRIBUTE+"}", String.valueOf(attributes.get(MIN_ATTRIBUTE)));
        return message;
    }
}

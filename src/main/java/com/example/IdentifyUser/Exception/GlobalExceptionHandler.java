package com.example.IdentifyUser.Exception;


import com.example.IdentifyUser.dto.reponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ApiResponse<String> response = new ApiResponse<>();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        return  ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
        ApiResponse<String> response = new ApiResponse<>();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        String errorKey = ex.getFieldError().getDefaultMessage();
        try{
            errorCode = ErrorCode.valueOf(errorKey);
        }catch (IllegalArgumentException e){

        }
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}

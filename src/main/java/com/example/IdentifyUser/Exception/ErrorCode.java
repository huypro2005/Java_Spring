package com.example.IdentifyUser.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception.", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(1001, "User not existed.", HttpStatus.NOT_FOUND),
    USER_EXISTS(1002, "Username already exists.", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED_ACCESS(1005, "Unauthenticated access.", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1006, "Token has expired.", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(1007, "Token is invalid.", HttpStatus.BAD_REQUEST),
    TOKEN_GENERATION_FAILED(1008, "Token generation failed.", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1009, "Error key", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS(1010, "Unauthorized access.", HttpStatus.FORBIDDEN);


    private String message;
    private int code;
    private HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

}

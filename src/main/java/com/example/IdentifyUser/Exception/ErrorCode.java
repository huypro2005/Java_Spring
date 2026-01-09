package com.example.IdentifyUser.Exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception."),
    USER_NOT_FOUND(4440, "User not found."),
    USER_EXISTS(1001, "Username already exists."),
    USERNAME_INVALID(1002, "Username must be at least 3 characters"),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters"),
    INVALID_KEY(4444, "Error key");

    private final String message;
    private final int code;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public int getCode() {
        return code;
    }
}

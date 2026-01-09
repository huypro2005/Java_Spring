package com.example.IdentifyUser.Exception;


import lombok.Data;

public class AppException extends RuntimeException{
    private ErrorCode errorCode;
    public AppException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode(){
        return this.errorCode;
    }
    public void setErrorCode(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}

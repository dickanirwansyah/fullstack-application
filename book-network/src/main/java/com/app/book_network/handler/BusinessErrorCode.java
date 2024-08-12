package com.app.book_network.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum BusinessErrorCode {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED, "No code"),
    ACCOUNT_LOCKED(320, HttpStatus.FORBIDDEN ,"User account is locked"),
    INCORRECT_CURRENT_PASSWORD (300, HttpStatus.BAD_REQUEST, "Current password incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, HttpStatus.BAD_REQUEST, "The new password does not match"),
    ACCOUNT_DISABLED (303, HttpStatus.FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS (304, HttpStatus.FORBIDDEN, "Login and / or password incorrect"),;


    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCode(int code, HttpStatus httpStatus, String description){
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}

package com.nta.identity.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    AVATAR_BASE64_REQUIRED(1009, "avatarBase64 field is required", HttpStatus.BAD_REQUEST),
    INVALID_PROFILE_TYPE(10010, "Invalid profileType", HttpStatus.BAD_REQUEST),
    INVALID_ACCOUNT_TYPE(10011, "Invalid accountType", HttpStatus.BAD_REQUEST),
    IDENTITY_FRONT_BASE64_REQUIRED(10012, "identityFBase64 field is required", HttpStatus.BAD_REQUEST),
    IDENTITY_BACK_BASE64_REQUIRED(10013, "identityBBase64 field is required", HttpStatus.BAD_REQUEST),
    PROFILE_TYPE_REQUIRED(10014, "profileType field is required", HttpStatus.BAD_REQUEST),
    ACCOUNT_TYPE_REQUIRED(10015, "accountType field is required", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}

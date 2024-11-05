package com.nta.profileservice.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_PROFILE_TYPE(1002, "Invalid profileType", HttpStatus.BAD_REQUEST),
    PROFILE_NOT_FOUND(1003, "Profile not found", HttpStatus.NOT_FOUND),
    VEHICLE_NOT_FOUND(1004, "Vehicle not found", HttpStatus.NOT_FOUND),
    AVATAR_BASE64_REQUIRED(1005, "avatarBase64 field is required", HttpStatus.BAD_REQUEST),
    IDENTITY_FRONT_BASE64_REQUIRED(1006, "identityFBase64 field is required", HttpStatus.BAD_REQUEST),
    IDENTITY_BACK_BASE64_REQUIRED(1007, "identityBBase64 field is required", HttpStatus.BAD_REQUEST),
    VEHICLE_NUMBER_REQUIRED(1008, "vehicleNumber field is required", HttpStatus.BAD_REQUEST),
    VEHICLE_ID_REQUIRED(1009, "vehicleId field is required", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
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

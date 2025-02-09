package com.nta.postservice.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    CANNOT_UPLOAD_IMAGE(1008, "Cannot upload image", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_CATEGORY_NOT_FOUND(1009, "Product category not found", HttpStatus.BAD_REQUEST),
    POST_NOT_FOUND(1010, "Post not found", HttpStatus.NOT_FOUND),
    INVALID_POST_STATUS(1011, "Invalid post status", HttpStatus.BAD_REQUEST),
    VEHICLE_NOT_FOUND(1012, "Vehicle not found", HttpStatus.BAD_REQUEST),
    JOINED_POST_BEFORE(1013, "Joined post before", HttpStatus.BAD_REQUEST),
    OBJECT_NOT_FOUND(1014, "Object not found", HttpStatus.BAD_REQUEST),
    POST_TAKEN_BY_ANOTHER_SHIPPER(1015, "The post has already been taken by another shipper", HttpStatus.BAD_REQUEST),
    SHIPPER_POST_NOT_FOUND(1016, "Shipper post not found", HttpStatus.NOT_FOUND),
    RATING_NOT_FOUND(1017, "Rating not found", HttpStatus.NOT_FOUND),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}

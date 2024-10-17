package com.nta.profileservice.exception;

import java.util.Map;
import java.util.Objects;

import com.nta.profileservice.enums.ErrorCode;
import jakarta.validation.ConstraintViolation;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.nta.profileservice.components.Utils;
import com.nta.profileservice.dto.response.ApiResponse;
import com.nta.profileservice.enums.ProfileType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";
    private final Utils utils;

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(final RuntimeException exception) {
        log.error("Exception: ", exception);
        final ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(final AppException exception) {
        final ErrorCode errorCode = exception.getErrorCode();
        final ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handlingValidation(final HttpMessageNotReadableException exception)
            throws JsonProcessingException {
        String message = exception.getMessage();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(message);
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        if (exception.getCause() instanceof InvalidFormatException) {
            final InvalidFormatException invalidFormatException = (InvalidFormatException) exception.getCause();
            if (invalidFormatException.getTargetType().equals(ProfileType.class)) {
                apiResponse.setCode(ErrorCode.INVALID_PROFILE_TYPE.getCode());
                apiResponse.setMessage(ErrorCode.INVALID_PROFILE_TYPE.getMessage());

                apiResponse.setDetails("Except values: " + utils.writeEnumValuesAsString(ProfileType.class));
            }
        }
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<ApiResponse> handlingValidation(final IllegalArgumentException exception)
            throws JsonProcessingException {
        String message = exception.getMessage();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(message);
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        if (exception.getMessage().contains(ProfileType.class.getSimpleName())) {
            apiResponse.setCode(ErrorCode.INVALID_PROFILE_TYPE.getCode());
            apiResponse.setMessage(ErrorCode.INVALID_PROFILE_TYPE.getMessage());

            apiResponse.setDetails("Except values: " + utils.writeEnumValuesAsString(ProfileType.class));
        }
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(final MethodArgumentNotValidException exception) {
        final String enumKey = exception.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey); // Lấy ra enum Errorcode bằng tên mà đã được truyền khi validate

            var constraintViolation =
                    exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());

        } catch (IllegalArgumentException e) {

        }

        final ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(final String message, final Map<String, Object> attributes) {
        final String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}

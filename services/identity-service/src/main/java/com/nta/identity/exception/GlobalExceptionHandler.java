package com.nta.identity.exception;

import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.nta.identity.dto.response.ApiResponse;
import com.nta.identity.enums.AccountType;
import com.nta.identity.enums.ErrorCode;
import com.nta.identity.enums.ProfileType;
import com.nta.identity.utils.Utils;

import feign.FeignException;
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

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(final AccessDeniedException exception) {
        final ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handlingValidation(final HttpMessageNotReadableException exception)
            throws JsonProcessingException {
        String message = exception.getMessage();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(message);
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        if (exception.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getTargetType().equals(ProfileType.class)) {
                apiResponse.setCode(ErrorCode.INVALID_PROFILE_TYPE.getCode());
                apiResponse.setMessage(ErrorCode.INVALID_PROFILE_TYPE.getMessage() + ". Except values: "
                        + utils.writeEnumValuesAsString(ProfileType.class));
            } else if (invalidFormatException.getTargetType().equals(AccountType.class)) {
                apiResponse.setCode(ErrorCode.INVALID_ACCOUNT_TYPE.getCode());
                apiResponse.setMessage(ErrorCode.INVALID_ACCOUNT_TYPE.getMessage() + ". Except values: "
                        + utils.writeEnumValuesAsString(AccountType.class));
            }
        }
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = FeignException.class)
    ResponseEntity<ApiResponse> handlingValidation(final FeignException exception) {
        final ApiResponse apiResponse = new ApiResponse();
        try {
            // Tách mã trạng thái và nội dung phản hồi
            // Error message example: [400] during [POST] to [http://localhost:8081/profile/shippers]
            // [ProfileClient#createShipperProfile(ShipperProfileCreationRequest)]: [{"code":1006,"message":"Identity
            // front base64 is required"}]
            String[] parts = exception.getMessage().split(": ", 2);
            if (parts.length == 2) {
                // Phần sau dấu hai chấm chứa JSON
                String jsonPart = parts[1].trim();

                // Chuyển đổi JSON thành đối tượng
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonPart);
                if (jsonNode.isArray() && !jsonNode.isEmpty()) {
                    JsonNode errorDetail = jsonNode.get(0);
                    int code = errorDetail.get("code").asInt();
                    String message = errorDetail.get("message").asText();

                    apiResponse.setCode(code);
                    apiResponse.setMessage(message);
                }
            }
        } catch (Exception parseException) {
            System.err.println("Failed to parse error response: " + parseException.getMessage());
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

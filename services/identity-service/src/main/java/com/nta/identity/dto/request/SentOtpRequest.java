package com.nta.identity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SentOtpRequest {
    @NotNull(message = "NOT_NULL")
    @NotBlank(message = "NOT_BLANK")
    String toEmail;
    String username;
}

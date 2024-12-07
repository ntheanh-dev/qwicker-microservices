package com.nta.profileservice.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadImageRequest {
    @NotNull
    private Boolean isMultiple;

    byte[] base64;

    private List<byte[]> base64List;
}

package com.nta.fileservice.dto.response;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadImageResponse {
    @NotNull
    private Boolean isMultiple;

    String url;

    private List<String> urls;
}

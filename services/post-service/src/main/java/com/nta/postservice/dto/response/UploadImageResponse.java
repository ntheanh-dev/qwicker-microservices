package com.nta.postservice.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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

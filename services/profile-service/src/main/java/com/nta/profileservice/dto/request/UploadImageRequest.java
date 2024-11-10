package com.nta.profileservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadImageRequest {
    @NotNull
    private Boolean isMultiple;

    String base64;

    private List<String> base64List;
}

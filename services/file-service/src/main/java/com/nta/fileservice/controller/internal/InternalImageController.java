package com.nta.fileservice.controller.internal;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nta.fileservice.dto.request.UploadImageRequest;
import com.nta.fileservice.dto.response.ApiResponse;
import com.nta.fileservice.dto.response.UploadImageResponse;
import com.nta.fileservice.service.FileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/internal/images")
public class InternalImageController {

    FileService fileService;

    @PostMapping("/upload")
    public ApiResponse<UploadImageResponse> uploadImage(@RequestBody @Valid UploadImageRequest request) {
        return ApiResponse.<UploadImageResponse>builder()
                .result(fileService.uploadImage(request))
                .build();
    }
}

package com.nta.fileservice.service;

import org.springframework.stereotype.Service;

import com.nta.fileservice.dto.request.UploadImageRequest;
import com.nta.fileservice.dto.response.UploadImageResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {
    CloudinaryService cloudinaryService;

    public UploadImageResponse uploadImage(final UploadImageRequest request) {
        final UploadImageResponse response = new UploadImageResponse();
        response.setIsMultiple(request.getIsMultiple());

        if (request.getIsMultiple()) {
            response.setUrls(cloudinaryService.uploadImages(request.getBase64List()));
        } else {
            response.setUrl(cloudinaryService.uploadImage(request.getBase64()));
        }
        return response;
    }
}

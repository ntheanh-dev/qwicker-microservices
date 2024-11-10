package com.nta.profileservice.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nta.profileservice.dto.request.UploadImageRequest;
import com.nta.profileservice.dto.response.UploadImageResponse;

@FeignClient(name = "file-service", url = "http://localhost:8084/file")
public interface FileClient {
    @PostMapping(value = "/internal/images", produces = MediaType.APPLICATION_JSON_VALUE)
    UploadImageResponse uploadImage(@RequestBody UploadImageRequest request);
}

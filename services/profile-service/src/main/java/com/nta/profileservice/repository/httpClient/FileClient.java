package com.nta.profileservice.repository.httpClient;

import com.nta.profileservice.dto.request.UploadImageRequest;
import com.nta.profileservice.dto.response.UploadImageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "file-service")
public interface FileClient {
    @PostMapping(value = "/file/internal/images", produces = MediaType.APPLICATION_JSON_VALUE)
    UploadImageResponse uploadImage(@RequestBody UploadImageRequest request);
}

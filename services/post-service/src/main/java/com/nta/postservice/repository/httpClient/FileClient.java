package com.nta.postservice.repository.httpClient;

import com.nta.postservice.configuration.AuthenticationRequestInterceptor;
import com.nta.postservice.dto.request.UploadImageRequest;
import com.nta.postservice.dto.response.ApiResponse;
import com.nta.postservice.dto.response.UploadImageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "file-service", url = "${application.config.file-url}", configuration = {AuthenticationRequestInterceptor.class})
public interface FileClient {
    @PostMapping(value = "/internal/images/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UploadImageResponse> uploadImage(@RequestBody UploadImageRequest request);
}

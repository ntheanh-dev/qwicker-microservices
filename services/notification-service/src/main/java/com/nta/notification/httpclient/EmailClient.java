package com.nta.notification.httpclient;

import com.nta.notification.dto.request.EmailRequest;
import com.nta.notification.dto.response.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "emailClient", url = "https://api.brevo.com")
@Repository
public interface EmailClient {
    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    EmailResponse sentEmail(@RequestHeader("api-key") String apiKey, @RequestBody EmailRequest body);
}

package com.nta.websocket.repository.httpClient;

import com.nta.websocket.dto.response.Account;
import com.nta.websocket.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "identity-service", url = "${application.config.identity-url}")
public interface IdentityClient {
    @PostMapping("/internal/accounts/find-by-ids")
    ApiResponse<List<Account>> findAllAccountsByIds(@RequestParam("ids") Set<String> ids);
}

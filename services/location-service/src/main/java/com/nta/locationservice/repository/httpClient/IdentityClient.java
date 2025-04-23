package com.nta.locationservice.repository.httpClient;

import com.nta.locationservice.dto.request.internal.ChangeAccountStatusRequest;
import com.nta.locationservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "identity-service")
public interface IdentityClient {
    @PostMapping("/identity/internal/accounts/shippers/{accountId}/ready-for-take-order")
    ApiResponse<Boolean> isReadyForTakeOrder(@PathVariable String accountId);

    @PostMapping("/identity/internal/accounts/{accountId}/change-status")
    ApiResponse<?> changeStatusById(
            @PathVariable("accountId") String id, @RequestBody ChangeAccountStatusRequest request);
}

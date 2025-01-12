package com.nta.identity.controller.internal;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.*;

import com.nta.identity.dto.request.ChangeAccountStatusRequest;
import com.nta.identity.dto.response.ApiResponse;
import com.nta.identity.entity.Account;
import com.nta.identity.service.AccountService;
import com.nta.identity.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/internal/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalAccountController {
    AccountService accountService;
    AuthenticationService authenticationService;

    @PostMapping("/find-by-ids")
    ApiResponse<List<Account>> findAllByIds(@RequestBody Set<String> ids) {
        return ApiResponse.<List<Account>>builder()
                .result(accountService.findAllById(ids))
                .build();
    }

    @PostMapping("/change-status")
    ApiResponse<?> changeStatusById(@RequestBody ChangeAccountStatusRequest request) {
        final String accountId = authenticationService.getAccountIdFromCurrentToken();
        accountService.changeStatusById(accountId, request.getStatus());
        return ApiResponse.builder().build();
    }

    @PostMapping("/{accountId}/change-status")
    ApiResponse<?> changeStatusById(@PathVariable String accountId, @RequestBody ChangeAccountStatusRequest request) {
        accountService.changeStatusById(accountId, request.getStatus());
        return ApiResponse.builder().build();
    }

    @PostMapping("/{accountId}/is-online")
    ApiResponse<Boolean> isAccountOnline(@PathVariable String accountId) {
        return ApiResponse.<Boolean>builder()
                .result(accountService.isAccountOnline(accountId))
                .build();
    }
}

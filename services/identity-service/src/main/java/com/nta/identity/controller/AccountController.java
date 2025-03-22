package com.nta.identity.controller;

import com.nta.identity.dto.request.*;
import com.nta.identity.dto.response.AccountResponse;
import com.nta.identity.dto.response.ApiResponse;
import com.nta.identity.dto.response.DataExistResponse;
import com.nta.identity.dto.response.NumAccountsResponse;
import com.nta.identity.service.AccountService;
import com.nta.identity.service.AuthenticationService;

import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountController {
    AccountService accountService;
    AuthenticationService authenticationService;

    @PostMapping("/registration")
    ApiResponse<AccountResponse> createUser(@ModelAttribute @Valid AccountCreationRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.createAccount(request))
                .build();
    }

    @PostMapping("/registration/sent-otp")
    ApiResponse<?> creatAndSentOtp(@RequestBody @Valid SentOtpRequest request) {
        accountService.creatAndSentOtp(request);
        return ApiResponse.builder().build();
    }

    @PostMapping("/registration/verify-otp")
    ApiResponse<?> verifyOtp(@RequestBody @Valid OTPVerifyRequest request) {
        accountService.verifyOTP(request);
        return ApiResponse.builder().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ApiResponse<List<AccountResponse>> getAccounts(@RequestParam String accountType) {
        return ApiResponse.<List<AccountResponse>>builder()
                .result(accountService.getAccounts(accountType))
                .build();
    }

    @GetMapping("/{accountId}")
    ApiResponse<AccountResponse> getUser(@PathVariable("accountId") String accountId) {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.getAccount(accountId))
                .build();
    }

    @PostMapping("/change-status")
    ApiResponse<?> changeStatusById(@RequestBody ChangeAccountStatusRequest request) {
        final String accountId = authenticationService.getAccountIdFromCurrentToken();
        accountService.changeStatusById(accountId, request.getStatus());
        return ApiResponse.builder().build();
    }

    @DeleteMapping("/{accountId}")
    ApiResponse<String> deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{accountId}")
    ApiResponse<AccountResponse> updateAccount(
            @PathVariable String accountId, @RequestBody AccountUpdateRequest request) {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.updateAccount(accountId, request))
                .build();
    }

    @PostMapping("/check-username-exists")
    ApiResponse<DataExistResponse> checkIfUsernameExists(
            @ModelAttribute @Valid CheckUsernameExistsRequest request) {
        return ApiResponse.<DataExistResponse>builder()
                .result(accountService.checkIfUsernameExist(request))
                .build();
    }

    @PostMapping("/check-email-exists")
    ApiResponse<DataExistResponse> checkIfEmailExists(
            @ModelAttribute @Valid CheckEmailExistsRequest request) {
        return ApiResponse.<DataExistResponse>builder()
                .result(accountService.checkIfEmailExist(request))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total")
    ApiResponse<NumAccountsResponse> getNumAccounts() {
        return ApiResponse.<NumAccountsResponse>builder()
                .result(accountService.getNumAccounts())
                .build();
    }
}

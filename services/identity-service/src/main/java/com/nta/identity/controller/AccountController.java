package com.nta.identity.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nta.identity.dto.request.*;
import com.nta.identity.dto.response.AccountResponse;
import com.nta.identity.dto.response.ApiResponse;
import com.nta.identity.dto.response.DataExistResponse;
import com.nta.identity.service.AccountService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountController {
    AccountService accountService;

    @PostMapping("/registration")
    ApiResponse<AccountResponse> createUser(@RequestBody @Valid AccountCreationRequest request) {
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
    ApiResponse<?> verifyOtp(@RequestBody @Valid OTPverifyRequest request) {
        accountService.verifyOTP(request);
        return ApiResponse.builder().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ApiResponse<List<AccountResponse>> getAccounts() {
        return ApiResponse.<List<AccountResponse>>builder()
                .result(accountService.getAccounts())
                .build();
    }

    @GetMapping("/{accountId}")
    ApiResponse<AccountResponse> getUser(@PathVariable("accountId") String accountId) {
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.getAccount(accountId))
                .build();
    }

    //    @GetMapping("/my-info")
    //    ApiResponse<AccountResponse> getMyInfo() {
    //        return ApiResponse.<AccountResponse>builder()
    //                .result(accountService.getMyInfo())
    //                .build();
    //    }

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
    ApiResponse<DataExistResponse> checkIfUsernameExists(@RequestBody @Valid CheckUsernameExistsRequest request) {
        return ApiResponse.<DataExistResponse>builder()
                .result(accountService.checkIfUsernameExist(request))
                .build();
    }

    @PostMapping("/check-email-exists")
    ApiResponse<DataExistResponse> checkIfEmailExists(@RequestBody @Valid CheckEmailExistsRequest request) {
        return ApiResponse.<DataExistResponse>builder()
                .result(accountService.checkIfEmailExist(request))
                .build();
    }
}

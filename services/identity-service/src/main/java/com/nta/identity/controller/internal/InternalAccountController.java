package com.nta.identity.controller.internal;

import com.nta.identity.dto.response.ApiResponse;
import com.nta.identity.entity.Account;
import com.nta.identity.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/internal/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InternalAccountController {
    AccountService accountService;

    @PostMapping("/find-by-ids")
    ApiResponse<List<Account>> findAllByIds(@RequestBody Set<String> ids) {
        return ApiResponse.<List<Account>>builder()
                .result(accountService.findAllById(ids))
                .build();
    }

}

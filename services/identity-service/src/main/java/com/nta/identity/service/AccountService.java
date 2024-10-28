package com.nta.identity.service;

import java.util.HashSet;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nta.identity.constant.PredefinedRole;
import com.nta.identity.dto.request.AccountCreationRequest;
import com.nta.identity.dto.request.AccountUpdateRequest;
import com.nta.identity.dto.response.AccountResponse;
import com.nta.identity.entity.Account;
import com.nta.identity.entity.Role;
import com.nta.identity.enums.AccountType;
import com.nta.identity.enums.ErrorCode;
import com.nta.identity.exception.AppException;
import com.nta.identity.mapper.AccountMapper;
import com.nta.identity.mapper.ProfileMapper;
import com.nta.identity.repository.AccountRepository;
import com.nta.identity.repository.RoleRepository;
import com.nta.identity.repository.httpClient.ProfileClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AccountService {
    AccountRepository accountRepository;
    RoleRepository roleRepository;
    AccountMapper accountMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    ProfileMapper profileMapper;

    @Transactional
    public AccountResponse createAccount(final AccountCreationRequest request) {
        if (accountRepository.existsByUsername(request.getUsername())) throw new AppException(ErrorCode.USER_EXISTED);

        Account account = accountMapper.toAccount(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        final HashSet<Role> roles = new HashSet<>();
        if (request.getAccountType().equals(AccountType.SHIPPER)) {
            roleRepository.findById(PredefinedRole.SHIPPER_ROLE).ifPresent(roles::add);
            account.setRoles(roles);
            account = accountRepository.save(account);

            //  Call profile-service to create a Profile
            final var shipperProfileRequest = profileMapper.toShipperProfileCreationRequest(request);
            shipperProfileRequest.setAccountId(account.getId());
            final var shipperProfileResponse = profileClient.createShipperProfile(shipperProfileRequest);

            log.info("Created shipper profile: {}", shipperProfileResponse);
        } else {
            roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

            account.setRoles(roles);
            account = accountRepository.save(account);

            //  Call profile-service to create a Profile
            final var userProfileRequest = profileMapper.toUserProfileCreationRequest(request);
            userProfileRequest.setAccountId(account.getId());
            final var userProfileResponse = profileClient.createUserProfile(userProfileRequest);

            log.info("Created user profile: {}", userProfileResponse);
        }
        return accountMapper.toAccountResponse(account);
    }

    //    public AccountResponse getMyInfo() {
    //        final var context = SecurityContextHolder.getContext();
    //        final String name = context.getAuthentication().getName();
    //
    //        final Account account =
    //                accountRepository.findByUsername(name).orElseThrow(() -> new
    // AppException(ErrorCode.USER_NOT_EXISTED));
    //
    //        return accountMapper.toAccountResponse(account);
    //    }

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse updateAccount(final String accountId, final AccountUpdateRequest request) {
        final Account account =
                accountRepository.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        accountMapper.updateAccount(account, request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        account.setRoles(new HashSet<>(roles));

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAccount(final String userId) {
        accountRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountResponse> getAccounts() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toAccountResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse getAccount(final String id) {
        return accountMapper.toAccountResponse(
                accountRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}

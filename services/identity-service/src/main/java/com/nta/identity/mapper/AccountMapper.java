package com.nta.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.nta.identity.dto.request.AccountCreationRequest;
import com.nta.identity.dto.request.AccountUpdateRequest;
import com.nta.identity.dto.response.AccountResponse;
import com.nta.identity.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(AccountCreationRequest request);

    AccountResponse toAccountResponse(Account account);

    @Mapping(target = "roles", ignore = true)
    void updateAccount(@MappingTarget Account account, AccountUpdateRequest request);
}

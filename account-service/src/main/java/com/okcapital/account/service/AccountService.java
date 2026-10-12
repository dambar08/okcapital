package com.okcapital.account.service;

import com.okcapital.account.dto.CreateAccountRequest;
import com.okcapital.account.entity.Account;
import com.okcapital.account.repository.AccountRepository;

import com.okcapital.shared.UuidV8Generator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account openAccount(CreateAccountRequest request) {
        Account account = new Account(
                UuidV8Generator.generate(),
                request.customerId(),
                request.accountType(),
                request.currency(),
                BigDecimal.ZERO,
                "ACTIVE");
        return accountRepository.save(account);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}

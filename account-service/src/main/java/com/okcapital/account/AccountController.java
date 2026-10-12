package com.okcapital.account;

import com.okcapital.account.dto.CreateAccountRequest;
import com.okcapital.account.entity.Account;
import com.okcapital.account.service.AccountService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> list() {
        return accountService.findAll();
    }

    @PostMapping
    public ResponseEntity<Account> open(@RequestBody CreateAccountRequest request) {
        Account account = accountService.openAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
}

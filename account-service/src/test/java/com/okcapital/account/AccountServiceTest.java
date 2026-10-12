package com.okcapital.account;

import com.okcapital.account.dto.CreateAccountRequest;
import com.okcapital.account.entity.Account;
import com.okcapital.account.repository.AccountRepository;
import com.okcapital.account.service.AccountService;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @Test
    void openAccountStoresNewAccount() {
        AccountRepository repository = mock(AccountRepository.class);
        when(repository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AccountService service = new AccountService(repository);

        Account account = service.openAccount(new CreateAccountRequest(
                "cust-001",
                "LOAN",
                "USD"));

        assertNotNull(account.accountId());
        assertEquals(8, account.accountId().version());
        assertEquals("cust-001", account.customerId());
        assertEquals("LOAN", account.accountType());
        assertEquals(0, account.balance().compareTo(new BigDecimal("0.00")));
    }
}

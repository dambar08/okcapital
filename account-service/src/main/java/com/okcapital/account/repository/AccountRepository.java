package com.okcapital.account.repository;

import com.okcapital.account.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, java.util.UUID> {
}

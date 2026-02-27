package com.khrism.corebank.account.repository;

import com.khrism.corebank.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
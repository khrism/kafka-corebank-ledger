package com.khrism.corebank.account.service;

import com.khrism.corebank.account.domain.Account;
import com.khrism.corebank.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import com.khrism.corebank.account.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account create(String accountNumber, String customerName) {

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .customerName(customerName)
                .balance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();

        return accountRepository.save(account);
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    public Account update(Long id, String customerName) {

    Account account = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

    account.setCustomerName(customerName);

    return accountRepository.save(account);
    }

    public Account deposit(Long id, BigDecimal  amount) {
    Account acc = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

    acc.setBalance(acc.getBalance().add(amount));
    return accountRepository.save(acc);
}

    public Account withdraw(Long id, BigDecimal  amount) {
    Account acc = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

    if (acc.getBalance().compareTo(amount) < 0) {
        throw new RuntimeException("Insufficient funds");
    }

    acc.setBalance(acc.getBalance().subtract(amount));
    return accountRepository.save(acc);
    }
}

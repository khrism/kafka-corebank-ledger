package com.khrism.corebank.account.controller;

import com.khrism.corebank.account.domain.Account;
import com.khrism.corebank.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // GET /accounts
    @GetMapping
    public List<Account> getAll() {
        return accountService.getAll();
    }

    // GET /accounts/{id}
    @GetMapping("/{id}")
    public Account getById(@PathVariable Long id) {
        return accountService.getById(id);
    }

    // POST /accounts
    @PostMapping
    public Account create(@RequestParam String accountNumber,
                          @RequestParam String customerName) {
        return accountService.create(accountNumber, customerName);
    }

    // PUT /accounts/{id}
    @PutMapping("/{id}")
    public Account update(@PathVariable Long id,
                          @RequestParam String customerName) {
        return accountService.update(id, customerName);
    }
}
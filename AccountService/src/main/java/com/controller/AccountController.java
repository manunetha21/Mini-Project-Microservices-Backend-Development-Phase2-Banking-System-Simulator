package com.controller;

import com.model.Account;
import com.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService service;
    public AccountController(AccountService service){ this.service = service; }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account account){
        Account saved = service.createAccount(account);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> get(@PathVariable String accountNumber){
        return service.findByAccountNumber(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{accountNumber}/balance")
    public ResponseEntity<Account> updateBalance(@PathVariable String accountNumber, @RequestBody Double newBalance){
        return ResponseEntity.ok(service.updateBalance(accountNumber, newBalance));
    }
}


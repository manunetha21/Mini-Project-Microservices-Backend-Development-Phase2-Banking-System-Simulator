package com.controller;

import com.model.Transaction;
import com.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(
            @RequestParam String accountNumber,
            @RequestParam double amount) {
        return ResponseEntity.ok(service.deposit(accountNumber, amount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(
            @RequestParam String accountNumber,
            @RequestParam double amount) {
        return ResponseEntity.ok(service.withdraw(accountNumber, amount));
    }
}

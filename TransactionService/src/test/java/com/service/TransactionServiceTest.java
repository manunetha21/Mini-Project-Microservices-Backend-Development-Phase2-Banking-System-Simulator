package com.service;

import com.client.AccountRestClient;
import com.client.NotificationRestClient;
import com.dto.NotificationRequest;
//import com.dto.AccountResponse; // your DTO
import com.model.Transaction;
import com.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    @InjectMocks
    TransactionService transactionService;

    @Mock
    TransactionRepository repo;

    @Mock
    AccountRestClient accountRestClient;

    @Mock
    NotificationRestClient notificationRestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void deposit_success_savesTransactionAndCallsNotification() {
        String acc = "RS0987";
        double initial = 3000.0;
        double amount = 50.0;
        AccountRestClient.AccountDto accDto = new AccountRestClient.AccountDto();
        accDto.setAccountNumber(acc);
        accDto.setBalance(initial);

        when(accountRestClient.getAccount(acc)).thenReturn(accDto);
        // accountRestClient.updateBalance returns void — do nothing
        when(repo.save(any(Transaction.class))).thenAnswer(i -> {
            Transaction t = i.getArgument(0);
            t.setId(UUID.randomUUID().toString());
            return t;
        });

        Transaction tx = transactionService.deposit(acc, amount);
        assertNotNull(tx);
        assertEquals("DEPOSIT", tx.getType());
        assertEquals(amount, tx.getAmount());
        assertEquals(acc, tx.getSourceAccount());
        assertEquals("SUCCESS", tx.getStatus());

        verify(accountRestClient).updateBalance(eq(acc), eq(initial + amount));
        verify(notificationRestClient).sendNotification(any(NotificationRequest.class));
    }

    @Test
    void withdraw_insufficientFunds_marksFailed_and_sendsNotification() {
        String acc = "RS0987";
        AccountRestClient.AccountDto accDto = new AccountRestClient.AccountDto();
        accDto.setAccountNumber(acc);
        accDto.setBalance(10);
        when(accountRestClient.getAccount(acc)).thenReturn(accDto);
        when(repo.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));

        Transaction tx = transactionService.withdraw(acc, 50);

        assertEquals("FAILED", tx.getStatus());
        verify(accountRestClient, never()).updateBalance(anyString(), anyDouble());
        verify(notificationRestClient).sendNotification(any(NotificationRequest.class));
    }
}

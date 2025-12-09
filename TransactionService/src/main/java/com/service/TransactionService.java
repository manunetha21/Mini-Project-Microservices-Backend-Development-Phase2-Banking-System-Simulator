package com.service;
import com.client.AccountRestClient;
import com.client.NotificationRestClient;
import com.dto.NotificationRequest;
import com.model.Transaction;
import com.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository repo;
    private final AccountRestClient accountRestClient;
    private final NotificationRestClient notificationRestClient;


    public String notificationFallback(NotificationRequest req, Throwable ex) {
        log.error("Notification service DOWN. correlationId={}, error={}",
                MDC.get("correlationId"), ex.getMessage());
        return "Notification stored locally (notification service down)";
    }

    public TransactionService(TransactionRepository repo,
                              AccountRestClient accountRestClient,
                              NotificationRestClient notificationRestClient) {
        this.repo = repo;
        this.accountRestClient = accountRestClient;
        this.notificationRestClient = notificationRestClient;
    }

    public Transaction deposit(String accountNumber, double amount) {
        // Fetch account
        setCorrelationId();

        log.info("DEPOSIT request received. account={}, amount={}, correlationId={}",
                accountNumber, amount, MDC.get("correlationId"));

        var acc = accountRestClient.getAccount(accountNumber);
        log.info("Fetched account details from AccountService. balance={}, correlationId={}",
                acc.getBalance(), MDC.get("correlationId"));

        double newBalance = acc.getBalance() + amount;
        accountRestClient.updateBalance(accountNumber, newBalance);

        log.info("Updated balance in AccountService to {}. correlationId={}",
                newBalance, MDC.get("correlationId"));
        // Save transaction
        Transaction tx = new Transaction();
        tx.setTransactionId("TXN-" + UUID.randomUUID());
        tx.setType("DEPOSIT");
        tx.setAmount(amount);
        tx.setStatus("SUCCESS");
        tx.setSourceAccount(accountNumber);
        repo.save(tx);
        log.info("Transaction saved to MongoDB txId={}, correlationId={}",
                tx.getTransactionId(), MDC.get("correlationId"));

        // Notify
        notificationRestClient.sendNotification(
                new NotificationRequest(accountNumber,
                        "Deposit of " + amount + " successful. New balance: " + newBalance)
        );
        log.info("Notification triggered for account {} correlationId={}",
                accountNumber, MDC.get("correlationId"));
        return tx;
    }

    public Transaction withdraw(String accountNumber, double amount) {
        setCorrelationId();

        log.info("WITHDRAW request received. account={}, amount={}, correlationId={}",
                accountNumber, amount, MDC.get("correlationId"));
        var acc = accountRestClient.getAccount(accountNumber);

        double newBalance = acc.getBalance() - amount;

        Transaction tx = new Transaction();
        tx.setTransactionId("TXN-" + UUID.randomUUID());
        tx.setType("WITHDRAW");
        tx.setAmount(amount);
        tx.setSourceAccount(accountNumber);

        if (newBalance < 0) {
            tx.setStatus("FAILED");
            repo.save(tx);
            log.warn("Withdrawal FAILED (insufficient funds). balance={}, correlationId={}",
                    acc.getBalance(), MDC.get("correlationId"));

            notificationRestClient.sendNotification(
                    new NotificationRequest(accountNumber,
                            "Withdrawal failed: insufficient funds")
            );

            return tx;
        }

        accountRestClient.updateBalance(accountNumber, newBalance);

        tx.setStatus("SUCCESS");
        repo.save(tx);
        log.info("Withdrawal success. New balance={}, correlationId={}",
                newBalance, MDC.get("correlationId"));
        notificationRestClient.sendNotification(
                new NotificationRequest(accountNumber,
                        "Withdrawal of " + amount + " successful. New balance: " + newBalance)
        );

        return tx;
    }
    private void setCorrelationId() {
        String existing = MDC.get("correlationId");
        if (existing == null) {
            String newId = UUID.randomUUID().toString();
            MDC.put("correlationId", newId);
            log.debug("Generated new correlationId={}", newId);
        }
    }
}

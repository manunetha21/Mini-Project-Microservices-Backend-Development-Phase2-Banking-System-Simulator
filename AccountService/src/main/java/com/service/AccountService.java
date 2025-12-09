
package com.service;
import com.model.Account;
import com.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository repo;
    public AccountService(AccountRepository repo){ this.repo = repo; }

    public Account createAccount(Account account){
        return repo.save(account);
    }

    public Optional<Account> findByAccountNumber(String accountNumber){
        return repo.findByAccountNumber(accountNumber);
    }

    public Account updateBalance(String accountNumber, double newBalance){
        var accOpt = repo.findByAccountNumber(accountNumber);
        if(accOpt.isEmpty()) throw new RuntimeException("Account not found");
        Account acc = accOpt.get();
        acc.setBalance(newBalance);
        return repo.save(acc);
    }
}

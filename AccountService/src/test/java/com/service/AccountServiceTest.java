package com.service;
import com.model.Account;
import com.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_savesAndReturns() {
        Account input = new Account();
        input.setAccountNumber("A1");
        input.setHolderName("Bob");
        input.setBalance(100.0);

        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        Account saved = accountService.createAccount(input);
        assertNotNull(saved);
        assertEquals("A1", saved.getAccountNumber());
        verify(accountRepository).save(input);

    }
    @Test
    void updateBalance_updatesAndReturns() {
        Account existing = new Account();
        existing.setAccountNumber("A1");
        existing.setBalance(100.0);

        when(accountRepository.findByAccountNumber("A1")).thenReturn(Optional.of(existing));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        Account updated = accountService.updateBalance("A1", 150.0);
        assertEquals(150.0, updated.getBalance());
        verify(accountRepository).save(existing);
    }

    @Test
    void findByAccountNumber_notFound_returnsEmpty() {
        when(accountRepository.findByAccountNumber("NO")).thenReturn(Optional.empty());
        assertTrue(accountService.findByAccountNumber("NO").isEmpty());
    }
}

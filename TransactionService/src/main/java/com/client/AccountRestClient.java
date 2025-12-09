package com.client;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AccountRestClient {

    private final RestTemplate restTemplate;

    public AccountRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AccountDto getAccount(String accountNumber) {
        return restTemplate.getForObject(
                "http://ACCOUNTSERVICE/api/accounts/" + accountNumber,
                AccountDto.class
        );
    }

    public AccountDto updateBalance(String accountNumber, Double newBalance) {
        // Update the balance using PUT
        restTemplate.put(
                "http://ACCOUNTSERVICE/api/accounts/" + accountNumber + "/balance",
                newBalance
        );

        // Fetch the updated account and return it
        return restTemplate.getForObject(
                "http://ACCOUNTSERVICE/api/accounts/" + accountNumber,
                AccountDto.class
        );
    }


    public static class AccountDto {
        private String accountNumber;
        private String holderName;
        private double balance;

        // getters and setters
        public String getAccountNumber() { return accountNumber; }
        public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

        public String getHolderName() { return holderName; }
        public void setHolderName(String holderName) { this.holderName = holderName; }

        public double getBalance() { return balance; }
        public void setBalance(double balance) { this.balance = balance; }
    }
}


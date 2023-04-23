package com.example.demo.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.infrastructure.AccountRepository;
import com.example.demo.jpaentities.Account;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if there are any accounts in the DB
        if (accountRepository.count() == 0) {
            // Create some accounts and save them into DB
            Account account1 = new Account();
            account1.setIban("ES1234567890123456789012");
            account1.setBalance(BigDecimal.valueOf(1000.0));
            Account account2 = new Account();
            account2.setIban("ES1234512345123456789012");
            account2.setBalance(BigDecimal.valueOf(10000000.0));
            Account account3 = new Account();
            account3.setIban("ES1222222345123456789012");
            account3.setBalance(BigDecimal.valueOf(1.0));
            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);
        }
    }
}







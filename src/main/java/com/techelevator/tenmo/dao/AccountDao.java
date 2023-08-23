package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public interface AccountDao {
    <List> Account list();

    Account getAccount(int accountId);

    Account createAccount(Account account);

    void updateBalance(int accountId, BigDecimal updatedBalance);

    Account deleteAccountById (int accountId);




}

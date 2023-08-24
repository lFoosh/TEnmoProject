package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> list();

    Account getAccount(int accountId);

    BigDecimal getBalanceByUserID(int id);

    void updateBalance(int accountId, BigDecimal updatedBalance);

    Account deleteAccountById (int accountId);




}

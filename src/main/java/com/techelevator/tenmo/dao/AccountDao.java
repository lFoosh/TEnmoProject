package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    /**
     * Retrieves the balance of an account associated with a specific user ID.
     *
     * @param id The user ID for which the balance needs to be retrieved.
     * @return The account balance as a BigDecimal value.
     */
    BigDecimal getBalanceByUserID(int id);

    /**
     * Fetches an Account object based on a user ID.
     *
     * @param userId The user ID for which the account needs to be fetched.
     * @return The Account object containing account details.
     */
    Account getByUserId (int userId);




}

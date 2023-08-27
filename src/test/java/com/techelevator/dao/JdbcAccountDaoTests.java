package com.techelevator.dao;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private AccountDao sut;

    @Autowired
    private DataSource dataSource;

    @Before
    public void setUp(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getBalanceByUserID() {
        BigDecimal balance = sut.getBalanceByUserID(1001);
        BigDecimal expectedBalance = new BigDecimal("1000.00");
        assertEquals(expectedBalance, balance);
    }

    @Test
    public void getByUserId_returns_correct_account() {
        Account account = sut.getByUserId(1001);
        assertNotNull(account);
        assertEquals(2001, account.getAccountId());
        assertEquals(1001, account.getUserId());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }
}
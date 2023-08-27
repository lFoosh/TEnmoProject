package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransfersDao;
import com.techelevator.tenmo.dao.TransfersDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
public class JdbcTransfersDaoTests extends BaseDaoTests {

    private TransfersDao sut;

    @Autowired
    private DataSource dataSource;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransfersDao(jdbcTemplate);
    }

    @Test
    public void findAllTransfersForUser_ShouldReturnTransfers() {
        List<Transfers> transfers = sut.findAllTransfersForUser(1001);  // Assuming 1 is a valid user id for testing purposes
        assertNotNull(transfers);

        List<Transfers> badTransfers = sut.findAllTransfersForUser(9999);  // Assuming 1 is a valid user id for testing purposes
        assertTrue(badTransfers.isEmpty());
    }


    @Test
    public void testGetTransferById() {
        int transferId = 3001; // Replace with an actual transfer ID
        // Create an expected Transfer object with data you expect to be retrieved
        Transfers expectedTransfer = new Transfers();
        expectedTransfer.setTransferId(transferId);
        expectedTransfer.setSenderId(2001); // Replace with the actual sender ID
        expectedTransfer.setReceiverId(2002); // Replace with the actual receiver ID
        expectedTransfer.setTransferAmount(new BigDecimal("100.00")); // Replace with the actual amount
        expectedTransfer.setTransferStatus("Approved"); // Replace with the actual status
        Transfers result = sut.getTransferById(transferId);
        // Perform assertions on the 'result' using assertEquals
        assertEquals(expectedTransfer.getTransferId(), result.getTransferId());
        assertEquals(expectedTransfer.getSenderId(), result.getSenderId());
        assertEquals(expectedTransfer.getReceiverId(), result.getReceiverId());
        assertEquals(expectedTransfer.getTransferAmount(), result.getTransferAmount());
        assertEquals(expectedTransfer.getTransferStatus(), result.getTransferStatus());
    }

    @Test
    public void addToReceiverBalance_ShouldIncreaseBalance() {
        int receiverId = 2002; // Adjust based on your test data.
        BigDecimal initialBalance = getCurrentBalance(receiverId);
        BigDecimal amountToAdd = new BigDecimal("50.00");
        sut.addToReceiverBalance(amountToAdd, receiverId);
        BigDecimal finalBalance = getCurrentBalance(receiverId);
        assertEquals(initialBalance.add(amountToAdd), finalBalance);
    }

    private BigDecimal getCurrentBalance(int accountId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
    }


    @Test
    public void subtractFromSenderBalance_ShouldDecreaseBalance() {
        int senderId = 2001;
        BigDecimal initialBalance = getCurrentBalance1(senderId);
        BigDecimal amountToSubtract = new BigDecimal("50.00");

        assertTrue(initialBalance.compareTo(amountToSubtract) >= 0);
        sut.subtractFromSenderBalance(amountToSubtract, senderId);
        BigDecimal finalBalance = getCurrentBalance1(senderId);
        assertEquals(initialBalance.subtract(amountToSubtract), finalBalance);
    }

    private BigDecimal getCurrentBalance1(int accountId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
    }
    // Test 1: Positive case
    @Test
    public void createTransfer_ValidTransfer_ShouldReturnTransfer() {
        Transfers transfer = new Transfers();
        Account account = new Account();
        account.setAccountId(2001);
        account.setBalance(new BigDecimal("1000.00"));
        transfer.setSenderId(2001);
        transfer.setReceiverId(2002);
        transfer.setTransferAmount(new BigDecimal("100.00"));

        Transfers result = sut.createTransfer(transfer, account);

        assertNotNull(result);
        assertEquals(transfer.getTransferAmount(), result.getTransferAmount());
    }

    // Test 2: Transfer amount is not a multiple of 0.01
    @Test
    public void createTransfer_InvalidAmount_ShouldThrowException() {
        Transfers transfer = new Transfers();
        Account account = new Account();
        account.setAccountId(2001);
        transfer.setSenderId(2001);
        transfer.setReceiverId(2002);
        transfer.setTransferAmount(new BigDecimal("100.005"));

        assertThrows(IllegalArgumentException.class, () -> {
            sut.createTransfer(transfer, account);
        });
    }

    // Test 3: Insufficient funds
    @Test
    public void createTransfer_InsufficientFunds_ShouldThrowException() {
        Transfers transfer = new Transfers();
        Account account = new Account();
        account.setAccountId(2001);
        account.setBalance(new BigDecimal("50.00"));
        transfer.setSenderId(2001);
        transfer.setReceiverId(2002);
        transfer.setTransferAmount(new BigDecimal("100.00"));

        assertThrows(IllegalArgumentException.class, () -> {
            sut.createTransfer(transfer, account);
        });
    }

    // Test 4: Sender and Receiver are the same
    @Test
    public void createTransfer_SenderEqualsReceiver_ShouldThrowException() {
        Transfers transfer = new Transfers();
        Account account = new Account();
        account.setAccountId(2001);
        transfer.setSenderId(2001);
        transfer.setReceiverId(2001);
        transfer.setTransferAmount(new BigDecimal("100.00"));  // Make sure this is set.

        assertThrows(IllegalArgumentException.class, () -> {
            sut.createTransfer(transfer, account);
        });
    }


    // Test 5: Account ID mismatch with sender
    @Test
    public void createTransfer_AccountIdMismatch_ShouldThrowException() {
        Transfers transfer = new Transfers();
        Account account = new Account();
        account.setAccountId(2002);
        transfer.setSenderId(2001);
        transfer.setReceiverId(2003);
        transfer.setTransferAmount(new BigDecimal("100.00"));

        assertThrows(IllegalArgumentException.class, () -> {
            sut.createTransfer(transfer, account);
        });
    }


}

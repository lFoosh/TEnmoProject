package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTransfersDao implements TransfersDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransfersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfers> findAllTransfersForUser(int userId) {
        String sql = "SELECT * FROM transfers WHERE userId = ?";
        return jdbcTemplate.query(sql, new TransferRowMapper(), userId);
    }

    @Override
    public Transfers createTransfer(Transfers transfers) {
        return null;
    }

    @Override
    public Transfers getTransferById(int transferId) {
        return null;
    }

    @Override
    public int findIdBy(String username) {
        return 0;
    }
    /*
     * This method manages the transaction for transferring funds between accounts.
     * The @Transactional annotation ensures that if any part of the transaction fails
     * (for instance, money gets deducted from the sender but fails to add to the receiver),
     * then the entire operation is rolled back to maintain data integrity.
     */
   @Transactional
    public void transfer(int senderId, int receiverId,BigDecimal transferAmount){
        BigDecimal sendingAccountBalance = getCurrentBalance(senderId);
        if(transferAmount.compareTo(BigDecimal.ZERO) <= 0 || sendingAccountBalance.compareTo(transferAmount) < 0) {
            throw new IllegalArgumentException("Insufficient Funds.");
        }
            updateTransferBalance(senderId, transferAmount.negate());
            updateTransferBalance(receiverId, transferAmount);
        }

    public void updateTransferBalance(int userId, BigDecimal amount){
        String sql = "UPDATE account SET balance = balance + ? WHERE user_id = ?";
        jdbcTemplate.update(sql, amount, userId);
    }

    //current balance of a user
    public BigDecimal getCurrentBalance(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    private class TransferRowMapper implements RowMapper<Transfers> {
        @Override
        public Transfers mapRow(ResultSet rs, int rowNum) throws SQLException {
            Transfers transfer = new Transfers();
            transfer.setTransferId(rs.getInt("transfer_id"));
            transfer.setTransferAmount(rs.getBigDecimal("amount"));
            transfer.setSenderId(rs.getInt("sender_id"));
            transfer.setReceiverId(rs.getInt("receiver_id"));
            transfer.setTransferStatus(rs.getString("transfer_status"));
            return transfer;
        }
    }
}

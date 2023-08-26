package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransfersDao implements TransfersDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransfersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfers> findAllTransfersForUser(int userId) {
        String sql = "SELECT transfers.transfer_id, transfers.sender_id, transfers.receiver_id, transfers.amount, transfers.transfer_status " +
                "FROM transfers " +
                "JOIN account AS sender_account ON transfers.sender_id = sender_account.account_id " +
                "JOIN account AS receiver_account ON transfers.receiver_id = receiver_account.account_id " +
                "WHERE sender_account.user_id = ? OR receiver_account.user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        List<Transfers> transfers = new ArrayList<>();
        while (results.next()) {
            transfers.add(mapRowToTransfers(results));
        }
        return transfers;
    }

    @Override
    public Transfers getTransferById(int transferId) {
        Transfers transfers = null;
        String sql = "SELECT * FROM transfers WHERE transfer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfers = mapRowToTransfers(results);
            }
        }catch (Exception e){
            System.out.println("Exception! Probably not good.");
        }
        return transfers;
    }

    @Override
    public int findIdBy(String username) {
        return 0;
    }

    @Override
    @Transactional
    public Transfers createTransfer(Transfers transfers) {
        Transfers newTransfer = null;
        String sql = "INSERT INTO transfers (sender_id, receiver_id, amount, transfer_status)\n" +
                "VALUES (?,?, ?,'Approved') RETURNING transfer_id;";
        int senderId = transfers.getSenderId();
        int receiverId = transfers.getReceiverId();

        if (senderId == receiverId) {
            throw new IllegalArgumentException("Sender cannot be the same as the receiver.");
        }
        BigDecimal transferAmount = transfers.getTransferAmount();
        BigDecimal senderBalance = getCurrentBalance(senderId);
        if (transferAmount.compareTo(senderBalance) > ){
                throw new IllegalArgumentException("Cannot send more than current balance.");
            } else {
            Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, senderId, receiverId, transfers.getTransferAmount());
            newTransfer = getTransferById(transferId);
            subtractFromSenderBalance(transfers.getTransferAmount(), transfers.getSenderId());
            addToReceiverBalance(transfers.getTransferAmount(), transfers.getReceiverId());
            return newTransfer;
        }
    }

    @Override
    public void subtractFromSenderBalance(BigDecimal transferAmount, int senderId){
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        jdbcTemplate.update(sql, transferAmount, senderId);
    }

    @Override
    public void addToReceiverBalance(BigDecimal transferAmount, int receiverId){
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        jdbcTemplate.update(sql, transferAmount, receiverId);
    }


    //current balance of a user
    public BigDecimal getCurrentBalance(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }


        public Transfers mapRowToTransfers(SqlRowSet rs) {
            Transfers transfers = new Transfers();
            transfers.setTransferId(rs.getInt("transfer_id"));
            transfers.setTransferAmount(rs.getBigDecimal("amount"));
            transfers.setSenderId(rs.getInt("sender_id"));
            transfers.setReceiverId(rs.getInt("receiver_id"));
            transfers.setTransferStatus(rs.getString("transfer_status"));
            return transfers;
        }
    }

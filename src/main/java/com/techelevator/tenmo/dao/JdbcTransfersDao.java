package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
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
    @Transactional
    public Transfers createTransfer(Transfers transfers, Account account) {
        BigDecimal senderCurrentBalance = account.getBalance();
        if (account.getAccountId() != transfers.getSenderId())
            throw new IllegalArgumentException("You aint that guy Bruh");
        if (transfers.getTransferAmount().remainder(new BigDecimal("0.01")).compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("Invalid amount, must be a multiple of 0.01");
        }
        if (transfers.getTransferAmount().compareTo(senderCurrentBalance) == 1) {
            throw new IllegalArgumentException("Insufficient funds to make the transfer.");
        }
        if (transfers.getSenderId() == transfers.getReceiverId()) {
            throw new IllegalArgumentException("Sender cannot be the same as the receiver.");
        }
        String sql = "INSERT INTO transfers (sender_id, receiver_id, amount, transfer_status)\n" +
                "VALUES (?,?, ?,'Approved') RETURNING transfer_id";
        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, transfers.getSenderId(), transfers.getReceiverId(), transfers.getTransferAmount());

        subtractFromSenderBalance(transfers.getTransferAmount(), transfers.getSenderId());
        addToReceiverBalance(transfers.getTransferAmount(), transfers.getReceiverId());

        return getTransferById(transferId);
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

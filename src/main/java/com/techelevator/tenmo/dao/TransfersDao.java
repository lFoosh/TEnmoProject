package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfers;

import java.math.BigDecimal;
import java.util.List;

public interface TransfersDao {

        /**
         * Retrieves all transfers associated with a specific user.
         *
         * @param userId ID of the user.
         * @return List of Transfers.
         */
        List<Transfers> findAllTransfersForUser(int userId);

        /**
         * Creates a new transfer record in the system.
         *
         * @param transfers The transfer details.
         * @return The created transfer.
         */
        Transfers createTransfer(Transfers transfers);

        /**
         * Retrieves the details of a specific transfer using its ID.
         *
         * @param transferId ID of the transfer.
         * @return Transfer details.
         */
        Transfers getTransferById(int transferId);

        /**
         * Retrieves the user ID using a username.
         *
         * @param username Name of the user.
         * @return ID of the user.
         */
        int findIdBy(String username);

        void transfer(int senderId, int receiver, BigDecimal transferAmount);
        void updateTransferBalance(int userId, BigDecimal amount);
        BigDecimal getCurrentBalance(int userId);
    }


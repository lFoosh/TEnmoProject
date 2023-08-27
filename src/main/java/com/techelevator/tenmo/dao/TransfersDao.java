package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
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
        Transfers createTransfer(Transfers transfers, Account account);

        /**
         * Retrieves the details of a specific transfer using its ID.
         *
         * @param transferId ID of the transfer.
         * @return Transfer details.
         */
        Transfers getTransferById(int transferId);


        /**
         * This helper method subtracts from the balance of the sender account.
         * @param transferAmount
         * @param senderId
         */
        void subtractFromSenderBalance(BigDecimal transferAmount, int senderId);

        /**
         * This helper method adds to the balance of the receiver account.
         * @param transferAmount
         * @param receiverId
         */
        void addToReceiverBalance(BigDecimal transferAmount, int receiverId);

    }


package com.techelevator.tenmo.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Transfers {

    private int transferId;
    @Positive
    private BigDecimal transferAmount;
    private int senderId;
    private int receiverId;
    private String transferStatus;

    public Transfers() {
    }

    public Transfers(int transferId, BigDecimal transferAmount, int senderId, int receiverId, String transferStatus) {
        this.transferId = transferId;
        this.transferAmount = transferAmount;
        this.senderId = senderId; // Account subtracting from
        this.receiverId = receiverId; // Account adding to
        this.transferStatus = transferStatus;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }
}

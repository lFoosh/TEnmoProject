package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransfersDao;

public class TransfersController {

    private TransfersDao transfersDao;

    public TransfersController(TransfersDao transfersDao) {
        this.transfersDao = transfersDao;
    }
}

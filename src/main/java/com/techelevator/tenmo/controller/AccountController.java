package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountDao accountDao;
    private final UserDao userDao;  // Add this to get userId by username

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;   // Initialize the userDao
    }

    @GetMapping("/balance")
    @PreAuthorize("isAuthenticated()")
    public String getBalanceByUsername(Principal principal) {
        String currentUsername = principal.getName();
        int userId = userDao.findIdByUsername(currentUsername);  // Fetch the userId based on username

        BigDecimal balance = accountDao.getBalanceByUserID(userId);// Fetch the balance for the userId
        int intBalance = balance.intValue();

        return "{ \n" +
                "username: " + principal.getName() + ", \n" +
                "balance: " + intBalance + "\n }";
    }

}

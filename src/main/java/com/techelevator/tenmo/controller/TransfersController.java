package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransfersDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api")
public class TransfersController {

    private  AccountDao accountDao;
    private TransfersDao transfersDao;
    private UserDao userDao;

    private JdbcTemplate jdbcTemplate;

    public TransfersController(TransfersDao transfersDao, UserDao userDao, AccountDao accountDao) {
        this.transfersDao = transfersDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    public class RecipientUsers {
        private String username;

        public RecipientUsers(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    @RequestMapping(path = "/listusers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public List<RecipientUsers> listRecipients(Principal principal) {
        String currentUsername = principal.getName();

        List<User> users = userDao.findAll();
        List<RecipientUsers> usernames = new ArrayList<>();

        for (User user : users) {
            if (!user.getUsername().equals(currentUsername)) {
                usernames.add(new RecipientUsers(user.getUsername()));
            }
        }
        return usernames;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/createtransfer", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public Transfers createTransfer(@RequestBody Transfers transfers, Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        Account account= accountDao.getByUserId(userId);
            return transfersDao.createTransfer(transfers, account);

    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path="/completedtransfers", method= RequestMethod.GET)
    public List<Transfers> getCompletedTransfers(Principal principal){
        String currentUsername = principal.getName();
        int userId = userDao.findIdByUsername(currentUsername);
        return transfersDao.findAllTransfersForUser(userId);
    }

    @RequestMapping(path="/transfers/{transferId}", method = RequestMethod.GET)
    public Transfers getTransferById(@PathVariable int transferId) {
        return transfersDao.getTransferById(transferId);
    }






}

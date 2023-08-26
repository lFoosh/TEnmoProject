package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransfersDao;
import com.techelevator.tenmo.dao.UserDao;
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

    private TransfersDao transfersDao;
    private UserDao userDao;

    private JdbcTemplate jdbcTemplate;

    public TransfersController(TransfersDao transfersDao, UserDao userDao) {
        this.transfersDao = transfersDao;
        this.userDao = userDao;
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
    public Transfers createTransfer(@RequestBody Transfers transfers){
            return transfersDao.createTransfer(transfers);

    }
}

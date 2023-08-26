package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransfersDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api")
public class TransfersController {

    private TransfersDao transfersDao;
    private UserDao userDao;

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
}

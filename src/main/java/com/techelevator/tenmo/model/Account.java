package com.techelevator.tenmo.model;

import java.util.HashSet;
import java.util.Set;

public class Account {

    private int id;
    private String username;
    private String password;
    private boolean activated;
    private Set<Authority> authorities = new HashSet<>();

    public Account() {
    }

    public Account(int id, String username, String password, boolean activated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.activated = activated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public void setAuthorities(String authorities) {
        String[] roles = authorities.split(",");
        for (String role : roles) {
            this.authorities.add(new Authority("ROLE_" + role));
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", activated=" + activated +
                ", authorities=" + authorities +
                '}';
    }
}

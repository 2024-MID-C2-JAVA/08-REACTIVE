package com.bank.management.command;

import com.bank.management.generic.Command;

import java.util.List;

public class CreateUserCommand extends Command {

    private String name;
    private String lastname;
    private String username;
    private String password;
    private List<String> roles;

    public CreateUserCommand(String name, String lastname, String username, String password, List<String> roles) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public CreateUserCommand() {
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "CreateUserCommand{" +
                "uuid='" + uuid + '\'' +
                ", when=" + when +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}

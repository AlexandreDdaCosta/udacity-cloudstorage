package com.udacity.jwdnd.course1.cloudstorage.model;

import com.udacity.jwdnd.course1.cloudstorage.lib.CustomInvalidParameterException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class User {

    private Integer id;
    private String username;
    private String salt;
    private String password;
    private String firstName;
    private String lastName;

    public Integer getId() {
        return this.id;
    }
    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return this.username;
    }
    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getSalt() {
        return this.salt;
    }
    public User setSalt (String salt) {
        this.salt = salt;
        return this;
    }

    public String getPassword() {
        return this.password;
    }
    public User setPassword (String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }
    public User setFirstName (String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return this.lastName;
    }
    public User setLastName (String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User validate() {
        Pattern alphanumericNamePattern = Pattern.compile("^.*[A-Za-z0-9]+.*$");
        Pattern lowerCaseAlphanumericNamePattern = Pattern.compile("^.*[a-z0-9]+.*$");
        Pattern namePattern = Pattern.compile("^[A-Za-z0-9\\- ]+$");
        Pattern passwordPattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$");
        List<String> errorMessages = new ArrayList<>(Collections.emptyList());
        if (id != null && id < 1) {
            errorMessages.add("Internal data submission error.");
            throw new CustomInvalidParameterException(errorMessages);
        }
        if (username == null || username.trim().length() == 0) {
            errorMessages.add("User name is empty.");
        } else if (username.trim().length() > 20) {
            errorMessages.add("User name is too long; maximum 20 characters.");
        } else {
            username = username.trim();
            if (! lowerCaseAlphanumericNamePattern.matcher(username).matches()) {
                errorMessages.add("User name is invalid; only lower-case alphanumeric characters allowed.");
            }
        }
        if (password == null || password.trim().length() == 0) {
            errorMessages.add("Password is empty.");
        } else if (password.trim().length() < 8 || password.trim().length() > 20) {
            errorMessages.add("Password must be between 8 and 20 characters.");
        } else {
            password = password.trim();
            if (! passwordPattern.matcher(password).matches()) {
                errorMessages.add("Password is invalid; follow password rules.");
            }
        }
        if (firstName == null || firstName.trim().length() == 0) {
            errorMessages.add("First name is empty.");
        } else if (firstName.trim().length() > 20) {
            errorMessages.add("First name is too long; maximum 20 characters.");
        } else {
            firstName = firstName.trim();
            if (! namePattern.matcher(firstName).matches()) {
                errorMessages.add("Invalid characters in first name.");
            } else if (! alphanumericNamePattern.matcher(firstName).matches()) {
                errorMessages.add("First name has no alphanumeric characters.");
            }
        }
        if (lastName == null || lastName.trim().length() == 0) {
            errorMessages.add("Last name is empty.");
        } else if (lastName.trim().length() > 20) {
            errorMessages.add("Last name is too long; maximum 20 characters.");
        } else {
            lastName = lastName.trim();
            if (! namePattern.matcher(lastName).matches()) {
                errorMessages.add("Invalid characters in last name.");
            } else if (! alphanumericNamePattern.matcher(lastName).matches()) {
                errorMessages.add("Last name has no alphanumeric characters.");
            }
        }
        if (! errorMessages.isEmpty()) {
            throw new CustomInvalidParameterException(errorMessages);
        }
        return this;
    }
}

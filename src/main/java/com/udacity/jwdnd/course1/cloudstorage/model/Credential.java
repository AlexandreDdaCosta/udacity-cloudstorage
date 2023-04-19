package com.udacity.jwdnd.course1.cloudstorage.model;

import com.udacity.jwdnd.course1.cloudstorage.lib.CustomInvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Credential {

    private Integer id;
    private String url;
    private String username;
    private String encodedKey;
    private String password;
    private Integer userId;
    private String protocol = "";


    public Integer getId() { return this.id; }
    public Credential setId (Integer id) {
        this.id = id;
        return this;
    }

    public String getUrl() { return this.url; }
    public Credential setUrl (String url) {
        this.url = url;
        this.setProtocol(url);
        return this;
    }

    public String getUsername() { return this.username; }
    public Credential setUsername (String username) {
        this.username = username;
        return this;
    }

    public String getEncodedKey() { return this.encodedKey; }
    public Credential setEncodedKey (String encodedKey) {
        this.encodedKey = encodedKey;
        return this;
    }

    public String getPassword() { return this.password; }
    public Credential setPassword (String password) {
        this.password = password;
        return this;
    }

    public Integer getUserId() {
        return this.userId;
    }
    public Credential setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public String getProtocol() {
        return this.protocol;
    }
    public Credential setProtocol(String url) {
        if (! url.matches("^https?://.*$")) {
            this.protocol = "https://";
        } else {
            this.protocol = "";
        }
        return this;
    }

    public Credential validate() {
        Pattern urlRegex = Pattern.compile("^(https?:\\/\\/)?(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$");
        List<String> errorMessages = new ArrayList<>(Collections.emptyList());
        if ((userId == null || userId < 1) || (id != null && id < 1)) {
            errorMessages.add("Internal data submission error.");
            throw new CustomInvalidParameterException(errorMessages);
        }
        if (url == null || url.trim().length() == 0) {
            errorMessages.add("URL is empty.");
        }
        else {
            url = url.trim();
            if (url.length() > 100) {
                errorMessages.add("URL is too long; maximum 100 characters.");
            } else if (! urlRegex.matcher(url).matches()) {
                errorMessages.add("Invalid URL: [" + url + "]");
            }
        }
        if (username == null || username.trim().length() == 0) {
            errorMessages.add("User name is empty.");
        } else {
            username = username.trim();
            if (username.length() > 30) {
                errorMessages.add("User name is too long; maximum 30 characters.");
            }
        }
        if (password == null || password.trim().length() == 0) {
            errorMessages.add("Password is empty.");
        } else {
            password = password.trim();
            if (password.length() > 30) {
                errorMessages.add("Password is too long; maximum 30 characters.");
            }
        }
        if (! errorMessages.isEmpty()) {
            throw new CustomInvalidParameterException(errorMessages);
        }
        return this;
    }
}

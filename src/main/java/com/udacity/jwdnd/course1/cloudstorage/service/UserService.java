package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HashService hashService;

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }

    public void createUser(User user) {
        User insertUser = new User()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .validate();
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        insertUser.setSalt(encodedSalt);
        insertUser.setPassword(hashService.getHashedValue(user.getPassword(), encodedSalt));
        userMapper.insert(insertUser);
    }

    public User getUser(String username) {
        return userMapper.getUser(username);
    }

    public Integer getUserIdByName(String username) {
        User user = getUser(username);
        return user.getId();
    }
}

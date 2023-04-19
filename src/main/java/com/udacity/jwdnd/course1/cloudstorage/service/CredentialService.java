package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CredentialMapper credentialMapper;

    @Autowired
    private EncryptionService encryptionService;

    public Credential getUserCredential(Integer id, Integer userId) {
        return credentialMapper.getCredential(id, userId);
    }

    public List<Credential> getUserCredentials(Integer userId) {
        List<Credential> credentials = credentialMapper.getUserCredentials(userId);
        return credentials.stream()
                .map(this::decryptPassword)
                .collect(Collectors.toList());
    }

    public void addCredential(CredentialForm credentialForm) {
        Credential credential = new Credential()
                .setUrl(credentialForm.getUrl())
                .setUsername(credentialForm.getUsername())
                .setPassword(credentialForm.getPassword())
                .setUserId(userMapper.getUserIdByName(credentialForm.getAuthUsername()))
                .validate();
        credential = encryptPassword(credential);
        int addedCredentials = credentialMapper.addCredential(credential);
    }

    public void deleteCredential(Integer id, Integer userId) {
        credentialMapper.deleteCredential(id, userId);
    }

    public void updateCredential(Integer id, Integer userId, CredentialForm credentialForm) {
        Credential credential = new Credential()
                .setId(id)
                .setUrl(credentialForm.getUrl())
                .setUsername(credentialForm.getUsername())
                .setPassword(credentialForm.getPassword())
                .setUserId(userId)
                .validate();
        credential = encryptPassword(credential);
        credentialMapper.updateCredential(credential);
    }

    private Credential encryptPassword(Credential credential) {
        String encodedKey = RandomStringUtils.random(32, true, true);
        credential.setEncodedKey(encodedKey);
        return credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedKey));
    }

    public Credential decryptPassword(Credential credential) {
        return credential.setPassword(encryptionService.decryptValue(credential.getPassword(), credential.getEncodedKey()));
    }

    public Credential setProtocol(Credential credential) {
        return credential.setPassword(encryptionService.decryptValue(credential.getPassword(), credential.getEncodedKey()));
    }
}

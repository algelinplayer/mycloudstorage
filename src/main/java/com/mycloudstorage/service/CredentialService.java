package com.mycloudstorage.service;

import com.mycloudstorage.model.Credential;
import com.mycloudstorage.mapper.CredentialMapper;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int create(Credential credential) {
      String encodedKey = generateEncodedKey();
      String encryptedPassword = getEncryptedPassword(credential, encodedKey);
      credential.setKey(encodedKey);
      credential.setPassword(encryptedPassword);
      return credentialMapper.insert(credential);
    }

    private String getEncryptedPassword(Credential credential, String encodedKey) {
      String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
      return encryptedPassword;
    }

    private static String generateEncodedKey() {
      Random random = new Random();
      byte[] key = new byte[12];
      random.nextBytes(key);
      String encodedKey = Base64.getEncoder().encodeToString(key);
      return encodedKey;
    }

    public List<Credential> getCredentialsByUser(Integer userId) {
          return credentialMapper.getCredentialByUserId(userId);
      }

    public void update(Credential credential) {
      String encodedKey = generateEncodedKey();
      String encryptedPassword = getEncryptedPassword(credential, encodedKey);
      credential.setKey(encodedKey);
      credential.setPassword(encryptedPassword);
      credentialMapper.update(credential);
    }

    public void delete(Integer id) {
        credentialMapper.delete(id);
    }
}

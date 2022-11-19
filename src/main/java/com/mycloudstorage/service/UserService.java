package com.mycloudstorage.service;

import com.mycloudstorage.mapper.UserMapper;
import com.mycloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private UserMapper userMapper;
    private HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public boolean exists(String username) {
        return userMapper.getUser(username) == null;
    }

    public int create(User user) {
      String encodedSalt = generateAndEncodeSalt();
      String hashedPassword = hashPassword(user, encodedSalt);
      return userMapper.insertUser(new User(null, user.getUsername(), encodedSalt, hashedPassword, user.getFirstName(), user.getLastName()));
    }

    private String hashPassword(User user, String encodedSalt) {
      String hashedPassword = hashService.generateHashedValue(user.getPassword(), encodedSalt);
      return hashedPassword;
    }

    private static String generateAndEncodeSalt() {
      SecureRandom random = new SecureRandom();
      byte[] salt = new byte[16];
      random.nextBytes(salt);
      String encodedSalt = Base64.getEncoder().encodeToString(salt);
      return encodedSalt;
    }

    public User getUser(String username) {
        return userMapper.getUser(username);
    }
}

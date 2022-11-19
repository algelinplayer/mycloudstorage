package com.mycloudstorage.controller;

import com.mycloudstorage.model.Credential;
import com.mycloudstorage.model.User;
import com.mycloudstorage.service.CredentialService;
import com.mycloudstorage.service.EncryptionService;
import com.mycloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/credentials")
public class CredentialController {

    private UserService userService;
    private CredentialService credentialService;
    private EncryptionService encryptionService;

    public CredentialController(UserService userService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public String createOrUpdateCredential(Authentication authentication, Credential credential, Model model) {

        String username = authentication.getName();
        User user = userService.getUser(username);
        Integer userId = user.getUserId();

        if(credential.getCredentialId() == null) {
          if (createCredential(credential, userId) < 0) {
            return redirectSuccess(false);
          }
        } else {
          // TODO: Handle update errors and return "redirectSuccess(false)"
          updateCredential(credential);
        }

        return redirectSuccess(true);
    }

    private static String redirectSuccess(boolean x) {
      return "redirect:/result?isSuccess=" + x;
    }

    private void updateCredential(Credential credential) {
      credentialService.update(credential);
    }

    private int createCredential(Credential credential, Integer userId) {
      credential.setUserId(userId);
      int created = credentialService.create(credential);
      return created;
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId) {
        // TODO: Handle delete errors and return "redirectSuccess(false)"
        credentialService.delete(credentialId);
        return redirectSuccess(true);
    }

}

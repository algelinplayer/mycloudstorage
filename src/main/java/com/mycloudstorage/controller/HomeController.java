package com.mycloudstorage.controller;

import com.mycloudstorage.model.Credential;
import com.mycloudstorage.model.Note;
import com.mycloudstorage.model.User;
import com.mycloudstorage.service.CredentialService;
import com.mycloudstorage.service.FileService;
import com.mycloudstorage.service.NoteService;
import com.mycloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;

    public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, Note note, Credential credential, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        Integer userId = user.getUserId();

        model.addAttribute("files", fileService.getByUserId(userId));
        model.addAttribute("notes", noteService.getByUserId(userId));
        model.addAttribute("credentials", credentialService.getCredentialsByUser(userId));
        model.addAttribute("isFilesActive", "true");

        return "home";
    }
}

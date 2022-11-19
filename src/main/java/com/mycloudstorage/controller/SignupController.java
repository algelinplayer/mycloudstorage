package com.mycloudstorage.controller;

import com.mycloudstorage.model.User;
import com.mycloudstorage.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignUpPage() {
        return "signup";
    }

    @PostMapping
    public String signUp(@ModelAttribute User user, Model model) {
      createUserAndAddResult(user, model);
      return redirectLogin();
    }

    private void createUserAndAddResult(User user, Model model) {
      String error = null;
      String userName = user.getUsername();

      createUserAndAddResult(user, model, error, userName);
    }

    private void createUserAndAddResult(User user, Model model, String error, String userName) {

      if(!userService.exists(userName)) {
          error = "This Username [" + userName + "} is being used by other person.";
      } else {
        int created = createUser(user);
        if(created < 1) {
              error = "An error ocurred while trying to sign up. Please, try again.";
          }
      }

      if(error != null) {
          model.addAttribute("signupError", error);
      } else {
          model.addAttribute("signupSuccess", true);
      }

    }

    private int createUser(User user) {
      int created = userService.create(user);
      return created;
    }

    private static String redirectLogin() {
        return "redirect:/login";
    }

}

package com.mycloudstorage.controller;

import com.mycloudstorage.model.ErrorMessage;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorsController implements ErrorController{

    @RequestMapping("/error")
    public String handleError(Model model) {
        return "redirect:/result?isSuccess=" + false + "&error=" + ErrorMessage.PAGE_NOT_FOUND;
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}

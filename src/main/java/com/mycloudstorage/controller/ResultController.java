package com.mycloudstorage.controller;

import com.mycloudstorage.model.ErrorMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/result")
public class ResultController {

    @GetMapping
    public String getResultPage(Model model,
                                @RequestParam(required = false, name = "isSuccess") Boolean isSuccess,
                                @RequestParam(required = false, name = "error") ErrorMessage error) {

        if(error == null) error = ErrorMessage.UNKNOWN;

        model.addAttribute("errorType", error.getCode());
        model.addAttribute("errorMessage", error.getMessageDescription());
        model.addAttribute("isSuccess", isSuccess);

        return "result";
    }
}

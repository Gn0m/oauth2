package com.example.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogController {

    @GetMapping("/custom-logout")
    public String logout() {
        return "redirect:/login";
    }
}

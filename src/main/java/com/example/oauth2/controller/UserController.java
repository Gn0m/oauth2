package com.example.oauth2.controller;

import com.example.oauth2.dto.UserDTO;
import com.example.oauth2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/create")
    public String userCreate(Model model) {
        model.addAttribute("user", new UserDTO());
        return "signup";
    }

    @PostMapping("/create")
    public String userSave(@ModelAttribute("user") UserDTO userDTO) {
        userService.createUser(userDTO);
        return "redirect:/login";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id){
        userService.delete(id);
        return "redirect:/";
    }
}

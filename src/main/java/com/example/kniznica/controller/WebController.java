package com.example.kniznica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome/index"; // Assumes `src/main/resources/templates/welcome/index.html`
    }

}

package com.workintech.ecommerce.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/welcome")
public class Welcome {

    @GetMapping
    public String welcome(){
        return "welcome";
    }
}

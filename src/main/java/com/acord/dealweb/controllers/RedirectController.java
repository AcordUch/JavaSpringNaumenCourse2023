package com.acord.dealweb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RedirectController {
    @GetMapping("/")
    public ModelAndView redirectFromRoot() {
        return new ModelAndView("redirect:/ui/");
    }
}

package com.acord.dealweb.controllers;

import com.acord.dealweb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RegistrationController {
  private final UserService userService;

  @Autowired
  public RegistrationController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/signup")
  public ModelAndView redirectFromSignUp() {
    return new ModelAndView("redirect:/ui/registration");
  }

  @GetMapping("/registration")
  public ModelAndView redirectFromRegistration() {
    return new ModelAndView("redirect:/ui/registration");
  }
}

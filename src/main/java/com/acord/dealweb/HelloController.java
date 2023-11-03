package com.acord.dealweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class HelloController {
    private static final Random rnd = new Random();

    @GetMapping("/hi")
    public String index() {
        return String.format("Greetings from HelloController: %s", rnd.nextInt());
    }
}

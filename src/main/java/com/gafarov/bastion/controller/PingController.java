package com.gafarov.bastion.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class PingController extends BaseController {
    @CrossOrigin(origins = "http://94.241.140.221:8080")
    @GetMapping("/ping")
    public String ping() {
        return "pong pong pong";
    }
}

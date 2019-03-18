package com.github.skillz.mailserver.controller;

import com.github.skillz.mailserver.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private UserServiceImpl userService;

    @Value("${gmail.client.clientId}")
    private String clientId;

    @Value("${gmail.client.clientSecret}")
    private String clientSecret;

    @Value("${gmail.client.redirectUri}")
    private String redirectUri;

    @Value("${gmail.client.mySkillzBotUri}")
    private String mySkillzBotUri;


    @GetMapping(value = "/enable")
    public ResponseEntity<String> subscribe() {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping(value = "/disable")
    public ResponseEntity<String> unSubscribe() {
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}

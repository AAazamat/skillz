package com.github.skillz.mailserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class MailController {

    @PostMapping
    public String parse(@Valid @RequestBody Map<String, Object> request) {
        return "HELLOOOO, " + request.get("message");
    }

}

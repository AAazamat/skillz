package com.github.skillz.mailserver.controller;

import com.github.skillz.mailserver.service.UserService;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.GmailScopes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;

    private GoogleAuthorizationCodeFlow flow;

    @Value("${gmail.client.clientId}")
    private String clientId;

    @Value("${gmail.client.clientSecret}")
    private String clientSecret;

    @Value("${gmail.client.redirectUri}")
    private String redirectUri;

    @Value("${gmail.client.mySkillzBotUri}")
    private String mySkillzBotUri;


    @GetMapping(value = "/gmail")
    public ResponseEntity<String> login() {
        return new ResponseEntity<>(authorize(), HttpStatus.OK);
    }

    @GetMapping(value = "/gmailCallback", params = "code")
    public RedirectView oauth2Callback(@RequestParam(value = "code") String code) {
        log.info("code {}", code);

        try {
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();

            log.info("response {}", response);

            flow.createAndStoreCredential(response, "userID");
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }

        return new RedirectView(mySkillzBotUri);
    }

    private String authorize() {
        AuthorizationCodeRequestUrl authorizationUrl;
        try {
            if (flow == null) {
                Details web = new Details();
                web.setClientId(clientId);
                web.setClientSecret(clientSecret);
                GoogleClientSecrets clientSecrets = new GoogleClientSecrets().setWeb(web);
                flow = new GoogleAuthorizationCodeFlow
                       .Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets, Collections.singleton(GmailScopes.GMAIL_READONLY))
                       .build();
            }
            authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);

            log.info(authorizationUrl.build());
            return authorizationUrl.build();
        } catch (GeneralSecurityException|IOException exc) {
            log.error(exc.getLocalizedMessage(), exc);
            return "";
        }
    }
}

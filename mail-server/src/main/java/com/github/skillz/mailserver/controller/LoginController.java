package com.github.skillz.mailserver.controller;

import com.github.skillz.commons.UserDto;
import com.github.skillz.mailserver.service.UserService;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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

    private static final String APPLICATION_NAME = "MySkillzBot";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private HttpTransport httpTransport;
    private Gmail client;

    @Autowired
    private UserService userService;

    private UserDto user;

    private GoogleClientSecrets clientSecrets;
    private GoogleAuthorizationCodeFlow flow;
    private Credential credential;

    @Value("${gmail.client.clientId}")
    private String clientId;

    @Value("${gmail.client.clientSecret}")
    private String clientSecret;

    @Value("${gmail.client.redirectUri}")
    private String redirectUri;

    @Value("${gmail.client.mySkillzBotUri}")
    private String mySkillzBotUri;


    @GetMapping(value = "/gmail", params = "user")
    public ResponseEntity<String> login(@RequestParam(value = "user") UserDto user) {
        this.user = user;
        return new ResponseEntity<>(authorize(), HttpStatus.OK);
    }

    @GetMapping(value = "/gmailCallback", params = "code")
    public RedirectView oauth2Callback(@RequestParam(value = "code") String code) {

        log.info("code {}", code);

        JSONObject json = new JSONObject();
        JSONArray arr = new JSONArray();

        try {
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();

            credential = flow.createAndStoreCredential(response, "userID");

            client = new com.google.api.services.gmail.Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            String userId = "me";
            String query = "is:unread";
            ListMessagesResponse msgResponse = client.users().messages().list(userId).setQ(query).execute();

            for (Message msg : msgResponse.getMessages()) {
                Message message = client.users().messages().get(userId, msg.getId()).execute();
                log.info("snippet : {}", message.getSnippet());

                arr.put(message.getRaw());
            }
            json.put("response", arr);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }

        log.info("json {}", json);

        return new RedirectView(mySkillzBotUri);
    }

    private String authorize() {
        AuthorizationCodeRequestUrl authorizationUrl;
        try {
            if (flow == null) {
                Details web = new Details();
                web.setClientId(clientId);
                web.setClientSecret(clientSecret);
                clientSecrets = new GoogleClientSecrets().setWeb(web);
                httpTransport = GoogleNetHttpTransport.newTrustedTransport();
                flow = new GoogleAuthorizationCodeFlow
                       .Builder(httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(GmailScopes.GMAIL_READONLY))
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

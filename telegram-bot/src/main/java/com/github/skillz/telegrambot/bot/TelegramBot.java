package com.github.skillz.telegrambot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Objects;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${skillz.telegram.bot.user-name}")
    private String userName;

    @Value("${skillz.telegram.bot.bot-token}")
    private String botToken;

    @Value("${skillz.mail.server.api.url}")
    private String apiUrl;

    @PostConstruct
    private void postConstruct() {
        log.info(userName);
        log.info(botToken);
        log.info(apiUrl);
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info(update.toString());

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            SendMessage message;

            if ("/start".equalsIgnoreCase(messageText)) {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
                log.info(responseEntity.toString());
                message = new SendMessage().setChatId(chatId)
                        .setText(Objects.requireNonNull(responseEntity.getBody()));

            } else {
                message = new SendMessage().setChatId(chatId)
                                           .setText("Неизвестная команда");
            }

            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}


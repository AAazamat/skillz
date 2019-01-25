package com.training.skillz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class SkillzBot extends TelegramLongPollingBot {

    private SkillzBotProperties properties;

    public SkillzBot(SkillzBotProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    private void postConstruct() {
        log.info(properties.getBotToken());
        log.info(properties.getUserName());
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info(update.toString());

        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage().setChatId(chatId)
                    .setText(messageText);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return properties.getUserName();
    }

    @Override
    public String getBotToken() {
        return properties.getBotToken();
    }
}


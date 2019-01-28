package com.training.skillz.bot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("bot")
public class TelegramBotProperties {

    private String botToken;
    private String userName;
}

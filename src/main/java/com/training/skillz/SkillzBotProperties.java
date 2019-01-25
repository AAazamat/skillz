package com.training.skillz;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("bot")
public class SkillzBotProperties {

    private String botToken;
    private String userName;
}

package com.training.skillz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class SkillzApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(SkillzApplication.class, args);
	}

}
package com.missionx.questloggers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuestLoggersApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestLoggersApplication.class, args);
    }

}

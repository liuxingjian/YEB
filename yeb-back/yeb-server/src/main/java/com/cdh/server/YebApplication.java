package com.cdh.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

/**
 * @author by cdh
 * @description:
 * @Date: Created in 22:01 on 2021/4/16
 */
@EnableScheduling
@SpringBootApplication
public class YebApplication {
    public static void main(String[] args) {
        SpringApplication.run(YebApplication.class, args);
    }
}

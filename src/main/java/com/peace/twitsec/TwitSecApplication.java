package com.peace.twitsec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.peace.*")
public class TwitSecApplication {

    public static void main(String args[]) {
        SpringApplication.run(TwitSecApplication.class, args);
    }
}

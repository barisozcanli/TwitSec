package com.peace.twitsec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.peace.*")
@EnableAutoConfiguration
public class TwitSecApplication {

    public static void main(String args[]) {
        SpringApplication.run(TwitSecApplication.class, args);
    }
}

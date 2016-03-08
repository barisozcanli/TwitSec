package com.peace.twitsec.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.peace.*")
@EnableAutoConfiguration
public class AppConfig {
}

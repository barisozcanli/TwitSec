package com.peace.twitsec.config;

import com.peace.twitsec.schedulers.TwitterScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Bean
    public TwitterScheduler testSchedule() {
        return new TwitterScheduler();
    }
}

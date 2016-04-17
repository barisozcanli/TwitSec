package com.peace.twitsec.schedulers;

import com.peace.twitsec.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class TwitterScheduler {

    @Autowired
    private SchedulerService schedulerService;

    // 5 dakikada 1 çalışacak
    @Scheduled(fixedDelay = 5 * 60 * 1000 )
    public void checkFollowers() {

        schedulerService.checkNewOldFollowers();

    }
}

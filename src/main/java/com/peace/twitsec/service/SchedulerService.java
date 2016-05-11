package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.Follower;

import java.util.Set;

public interface SchedulerService {

    void checkNewOldFollowers() throws Exception;

}

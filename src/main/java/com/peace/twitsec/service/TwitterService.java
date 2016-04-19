package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.Follower;
import com.peace.twitsec.data.mongo.model.User;

import java.util.List;

public interface TwitterService {

    List<Long> getFollowerIds(User user);

    void sendDirectMessage(User user, long userId, String message);

    void sendDirectMessage(User user, List<Follower> userIdList, String message);
}

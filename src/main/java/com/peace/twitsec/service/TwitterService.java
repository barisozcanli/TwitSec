package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.User;

import java.util.List;

public interface TwitterService {

    List<Long> getFollowerIds(User user);
}

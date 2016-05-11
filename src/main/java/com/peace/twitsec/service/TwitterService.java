package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.Follower;
import com.peace.twitsec.data.mongo.model.TwitterUser;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.http.request.TwitterAuthenticationRequest;
import com.peace.twitsec.http.response.OauthConsumerResponse;
import com.peace.twitsec.http.response.OauthURLResponse;

import java.util.List;

public interface TwitterService {

    List<Long> getFollowerIds(User user) throws Exception;

    void tweet(User user, String message);

    void sendDirectMessage(User user, long userId, String message);

    void sendDirectMessage(User user, twitter4j.User twitterUser, String message);

    void blockUser(User user, twitter4j.User twitterUser, String reason);

    List<twitter4j.User> getUserProfiles(User user, List<Follower> userList);

    TwitterUser createTwitterUser(TwitterUser twitterUser);

    List<TwitterUser> createTwitterUsers(List<TwitterUser> twitterUserList);

    OauthURLResponse getOauthURL();

    OauthConsumerResponse getConsumerSecret(TwitterAuthenticationRequest request) throws Exception;

}

package com.peace.twitsec.service.impl;


import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.service.TwitSecService;
import com.peace.twitsec.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class TwitterServiceImpl extends TwitSecService implements TwitterService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<Long> getFollowerIds(User user) {

		List<Long> followerIds = new ArrayList<Long>();
		Twitter twitter = getTwitterInstance(user.getToken().getAccessToken(), user.getToken().getAccessTokenSecret());

		long[] ids = new long[0];
		try {
			ids = twitter.getFriendsIDs(-1).getIDs();
		} catch (TwitterException e) {
			//TODO handle this
			e.printStackTrace();
		}

		for (int i = 0; i < ids.length; i++) {
			followerIds.add(ids[i]);
		}
		return followerIds;
	}

	// FIXME
	private Twitter getTwitterInstance(String OAuthAccessToken, String OAuthAccessTokenSecret) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("consumerkeyhere")
				.setOAuthConsumerSecret("consumersecrethere")
				.setOAuthAccessToken(OAuthAccessToken)
				.setOAuthAccessTokenSecret(OAuthAccessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		return tf.getInstance();
	}

}
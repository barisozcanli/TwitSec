package com.peace.twitsec.service.impl;


import com.peace.twitsec.data.mongo.model.Follower;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.service.TwitSecService;
import com.peace.twitsec.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class TwitterServiceImpl extends TwitSecService implements TwitterService {

	@Value("${twitter.oauth.consumer.key}")
	private String consumerKey;

	@Value("${twitter.oauth.consumer.secret}")
	private String consumerSecret;


	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<Long> getFollowerIds(User user) {

		List<Long> followerIds = new ArrayList<Long>();
		Twitter twitter = getTwitterInstance(user.getToken().getAccessToken(), user.getToken().getAccessTokenSecret());

		long[] ids = new long[0];
		try {
			ids = twitter.getFollowersIDs(-1).getIDs();
		} catch (TwitterException e) {
			//TODO handle this
			e.printStackTrace();
		}

		for (int i = 0; i < ids.length; i++) {
			followerIds.add(ids[i]);
		}
		return followerIds;
	}

	public void tweet(User user, String message) {
		Twitter twitter = getTwitterInstance(user.getToken().getAccessToken(), user.getToken().getAccessTokenSecret());

		try {
			twitter.updateStatus(message);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	public void sendDirectMessage(User user, long userId, String message) {
		Twitter twitter = getTwitterInstance(user.getToken().getAccessToken(), user.getToken().getAccessTokenSecret());
		try {
			twitter.sendDirectMessage(userId, message);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	public void sendDirectMessage(User user, List<Follower> followerList, String message) {
		Twitter twitter = getTwitterInstance(user.getToken().getAccessToken(), user.getToken().getAccessTokenSecret());

		List<twitter4j.User> twitterProfiles = getUserProfiles(user, followerList);

		for(twitter4j.User twitterProfile: twitterProfiles) {
			int followerCount = twitterProfile.getFollowersCount();

			if(followerCount >= user.getPreferences().getNewFollowerFollowerCount()) {
				try {
					twitter.sendDirectMessage(twitterProfile.getId(), message);

					System.out.println("MESSAGE SENT | from : " + user.getUsername() + ", to twitterId :  " + twitterProfile.getId() + ", message : " + message);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<twitter4j.User> getUserProfiles(User user, List<Follower> userList) {
		List<twitter4j.User> twitterUserList = new ArrayList<twitter4j.User>();
		if(userList.size() == 0) {
			return twitterUserList;
		}

		Twitter twitter = getTwitterInstance(user.getToken().getAccessToken(), user.getToken().getAccessTokenSecret());

		long[] ids = new long[userList.size()];
		for(int i = 0; i < ids.length; i++) {
			ids[i] = userList.get(i).getTwitterId();
		}

		try {
			ResponseList<twitter4j.User> twitterResponseList =twitter.lookupUsers(ids);


			for(int i = 0; i < twitterResponseList.size(); i++) {
				twitter4j.User twitterUser = twitterResponseList.get(i);

				twitterUserList.add(twitterUser);
			}
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		return  twitterUserList;
	}

	// FIXME
	private Twitter getTwitterInstance(String OAuthAccessToken, String OAuthAccessTokenSecret) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(OAuthAccessToken)
				.setOAuthAccessTokenSecret(OAuthAccessTokenSecret);
		TwitterFactory tf = new TwitterFactory(cb.build());
		return tf.getInstance();
	}

}

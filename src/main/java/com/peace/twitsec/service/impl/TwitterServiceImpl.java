package com.peace.twitsec.service.impl;


import com.peace.twitsec.app.util.TwitterUtil;
import com.peace.twitsec.data.mongo.model.BlockReport;
import com.peace.twitsec.data.mongo.model.Follower;
import com.peace.twitsec.data.mongo.model.TwitterUser;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.TwitterUserRepository;
import com.peace.twitsec.http.request.TwitterAuthenticationRequest;
import com.peace.twitsec.http.response.OauthResponse;
import com.peace.twitsec.service.BlockReportService;
import com.peace.twitsec.service.MailService;
import com.peace.twitsec.service.TwitSecService;
import com.peace.twitsec.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TwitterServiceImpl extends TwitSecService implements TwitterService {

	@Value("${twitter.oauth.consumer.key}")
	private String consumerKey;

	@Value("${twitter.oauth.consumer.secret}")
	private String consumerSecret;

	@Autowired
	private MailService mailService;

	@Autowired
	private BlockReportService blockReportService;

	@Autowired
	private TwitterUserRepository twitterUserRepository;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public OauthResponse getOauthURL() {

		String url="";
		ConfigurationBuilder cb = new ConfigurationBuilder();

		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret);

		try {
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();

			RequestToken requestToken = twitter.getOAuthRequestToken();
			url = requestToken.getAuthorizationURL();
		} catch (Exception e) {}


		OauthResponse response = new OauthResponse();
		response.setUrl(url);
		return response;
	}

	public OauthResponse getConsumerSecret(TwitterAuthenticationRequest request) {

		String url="";
		ConfigurationBuilder cb = new ConfigurationBuilder();

		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret);

		try {
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();

			RequestToken requestToken = twitter.getOAuthRequestToken(request.getOauthToken());

			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, request.getVerifier());

			System.out.println("Access token: " + accessToken.getToken());
			System.out.println("Access token secret: " + accessToken.getTokenSecret());

		} catch (Exception e) {}


		OauthResponse response = new OauthResponse();
		response.setUrl(url);
		return response;
	}

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

			System.out.println("MESSAGE SENT | from : " + user.getUsername() + ", to twitterId :  " + userId + ", message : " + message);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	public void sendDirectMessage(User user, twitter4j.User twitterUser, String message) {
		sendDirectMessage(user, twitterUser.getId(), message);
	}

	public void blockUser(User user, twitter4j.User twitterUser, String patternsMatched) {
		Twitter twitter = getTwitterInstance(user.getToken().getAccessToken(), user.getToken().getAccessTokenSecret());

		try {
			twitter.createBlock(twitterUser.getId());

			String emailContent = "The user named " + twitterUser.getScreenName() + " is blocked \n";

			if (!emailContent.equals("")) {
				mailService.sendMail(user.getEmail(), "TwitSec Blocking User Notification", emailContent);
			}

			TwitterUser myTwitterUser = TwitterUtil.extractTwitterUser(twitterUser);
			myTwitterUser = createTwitterUser(myTwitterUser);

			BlockReport blockReport = new BlockReport();
			blockReport.setUser(user);
			blockReport.setTwitterId(twitterUser.getId());
			blockReport.setBlockReason(patternsMatched);
			blockReport.setCreatedAt(new Date());
			blockReport.setTwitterUser(myTwitterUser);

			List<BlockReport> blockReportList = new ArrayList<BlockReport>();
			blockReportList.add(blockReport);
			blockReportService.createBlockReports(blockReportList);

			System.out.println("BLOKED USER | " + user.getUsername() + "blocked twitterId :  " + twitterUser.getId());
		} catch (TwitterException e) {
			e.printStackTrace();
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

	public TwitterUser createTwitterUser(TwitterUser user) {

		TwitterUser existingUser = twitterUserRepository.findByTwitterId(user.getTwitterId());

		if(existingUser == null) {
			existingUser = twitterUserRepository.save(user);
		} else {
			existingUser.setTwitterId(user.getTwitterId());
			existingUser.setBiggerProfileImageURL(user.getBiggerProfileImageURL());
			existingUser.setDescription(user.getDescription());
			existingUser.setFollowersCount(user.getFollowersCount());
			existingUser.setFriendsCount(user.getFriendsCount());
			existingUser.setMiniProfileImageURL(user.getMiniProfileImageURL());
			existingUser.setName(user.getName());
			existingUser.setOriginalProfileImageURL(user.getOriginalProfileImageURL());
			existingUser.setProfileImageURL(user.getProfileImageURL());
			existingUser.setProfileBackgroundColor(user.getProfileBackgroundColor());
			existingUser.setProfileBackgroundImageURL(user.getProfileBackgroundImageURL());
			existingUser.setProfileTextColor(user.getProfileTextColor());
			existingUser.setURL(user.getURL());
			existingUser.setScreenName(user.getScreenName());

			twitterUserRepository.save(existingUser);
		}

		return existingUser;
	}

	public List<TwitterUser> createTwitterUsers(List<TwitterUser> twitterUserList) {
		List<TwitterUser> twitterUsers = twitterUserRepository.save(twitterUserList);

		return twitterUsers;
	}
}

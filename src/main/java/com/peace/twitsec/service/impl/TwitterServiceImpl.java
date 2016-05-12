package com.peace.twitsec.service.impl;


import com.peace.twitsec.app.util.TwitterUtil;
import com.peace.twitsec.data.mongo.model.*;
import com.peace.twitsec.data.mongo.repository.TwitterUserRepository;
import com.peace.twitsec.http.request.TwitterAuthenticationRequest;
import com.peace.twitsec.http.response.OauthConsumerResponse;
import com.peace.twitsec.http.response.OauthURLResponse;
import com.peace.twitsec.service.*;
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

	@Autowired AuthTokenService authTokenService;

	@Autowired UserService userService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public OauthURLResponse getOauthURL() {

		String url="";
		ConfigurationBuilder cb = new ConfigurationBuilder();

		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret);

		try {
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();

			RequestToken requestToken = twitter.getOAuthRequestToken();

			AuthToken authToken = new AuthToken();
			authToken.setToken(requestToken.getToken());
			authToken.setTokenSecret(requestToken.getTokenSecret());
			authTokenService.createAuthToken(authToken);


			url = requestToken.getAuthenticationURL();

			System.out.println("Authentication URL : " + url);
		} catch (Exception e) {}


		OauthURLResponse response = new OauthURLResponse();
		response.setUrl(url);
		return response;
	}

	public OauthConsumerResponse getConsumerSecret(TwitterAuthenticationRequest request) throws Exception {

		User user = userService.findByOauthToken(request.getOauthToken(), request.getVerifier());
		OauthConsumerResponse response = new OauthConsumerResponse();

		if (user == null) {
			AuthToken authToken = authTokenService.getAuthToken(request.getOauthToken());

			ConfigurationBuilder cb = new ConfigurationBuilder();

			cb.setDebugEnabled(true)
					.setOAuthConsumerKey(consumerKey)
					.setOAuthConsumerSecret(consumerSecret);

			AccessToken accessToken;
			Twitter twitter;
			try {
				TwitterFactory tf = new TwitterFactory(cb.build());
				twitter = tf.getInstance();

				RequestToken requestToken = new RequestToken(authToken.getToken(), authToken.getTokenSecret());

				accessToken = twitter.getOAuthAccessToken(requestToken, request.getVerifier());

				System.out.println("Access token: " + accessToken.getToken());
				System.out.println("Access token secret: " + accessToken.getTokenSecret());
				System.out.println("USERNAME: " + twitter.getScreenName());

			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}

			response.setAccessToken(accessToken.getToken());
			response.setAccessTokenSecret(accessToken.getTokenSecret());
			response.setUsername(twitter.getScreenName());
		} else {
			response.setAccessToken(user.getToken().getAccessToken());
			response.setAccessTokenSecret(user.getToken().getAccessTokenSecret());
			response.setUsername(user.getUsername());
		}

		return response;
	}

	public List<Long> getFollowerIds(User user) throws Exception {

		List<Long> followerIds = new ArrayList<Long>();
		Twitter twitter = getTwitterInstance(user.getToken().getAccessToken(), user.getToken().getAccessTokenSecret());

		long[] ids = new long[0];
		try {
			ids = twitter.getFollowersIDs(-1).getIDs();
		} catch (TwitterException e) {
			e.printStackTrace();
			throw new Exception("Couldn't retrieve followers!!!");
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
		System.out.println("Getting user profiles for the user : " + user.getUsername() + " === userList : " + userList.toString());
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
			System.out.println("couldn't lookup users for user " + user.getUsername());
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

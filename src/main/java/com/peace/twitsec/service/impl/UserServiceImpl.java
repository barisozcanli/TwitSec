package com.peace.twitsec.service.impl;

import com.peace.twitsec.app.common.ErrorCode;
import com.peace.twitsec.app.exception.TwitSecRuntimeException;
import com.peace.twitsec.app.util.KeyUtils;
import com.peace.twitsec.data.mongo.model.Token;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.model.UserPreferences;
import com.peace.twitsec.data.mongo.repository.UserRepository;
import com.peace.twitsec.data.session.TwitSecSession;
import com.peace.twitsec.http.request.*;
import com.peace.twitsec.http.response.LoginResponse;
import com.peace.twitsec.http.response.OauthConsumerResponse;
import com.peace.twitsec.service.TwitSecService;
import com.peace.twitsec.service.TwitterService;
import com.peace.twitsec.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends TwitSecService implements UserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;


	@Autowired
	private TwitterService twitterService;

	public User findById(String id) {
		User user = userRepository.findOne(id);

		if(user == null) {
			throw new TwitSecRuntimeException(400, ErrorCode.USER_NOT_FOUND, "");
	 	}

		return user;
	}

	@Override
	public User findByOauthToken(String oauthToken, String verifier) {
		return userRepository.findByOauthToken(oauthToken, verifier);
	}

	@Override
	public User createUser(CreateUserRequest request) {

		User user = userRepository.findByUsername(request.getUsername());
		if(user != null){
			throw new TwitSecRuntimeException(400, ErrorCode.DUPLICATE_USER, "");
		}

		User newUser = new User();

		newUser.setEmail(request.getEmail());
		newUser.setPassword(request.getPassword());
		newUser.setUsername(request.getUsername());

		UserPreferences preferences = new UserPreferences();
		preferences.setGoodByeTweetContent("Bye!");
		preferences.setLeftFollowerFollowerCount(0);
		preferences.setNewFollowerFollowerCount(0);
		preferences.setMentionOldFollowerInTweet(false);
		preferences.setSendAutoMessageToNewFollower(false);
		preferences.setNewFollowerAutoMessageContent("Welcome!");
		preferences.setWarnWithEmail(false);
		preferences.setUnwantedUsernamePatterns(new ArrayList<String>());

		newUser.setPreferences(preferences);

		Token token = new Token();
		token.setAccessToken(request.getAccessToken());
		token.setAccessTokenSecret(request.getAccessTokenSecret());
		newUser.setToken(token);

		user = userRepository.save(newUser);

		return user;
	}

	@Override
	public LoginResponse authenticate(AuthenticationRequest request) {

		LoginResponse response = new LoginResponse();

		// TODO password must be stored encrypted
		User user = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());

		if (user == null) {
			throw new TwitSecRuntimeException(400, ErrorCode.USER_NOT_FOUND, "");
		}

		response.setToken(KeyUtils.currentTimeUUID().toString());
		response.setId(user.getId());

		TwitSecSession.getInstance().addToken(response.getToken(), user);

		return response;
	}

	@Override
	public LoginResponse authenticateWithTwitter(TwitterAuthenticationRequest request) throws Exception {

		OauthConsumerResponse response = twitterService.getConsumerSecret(request);
		LoginResponse loginResponse = new LoginResponse();

		User user = userRepository.findByUsername(response.getUsername());

		if (user == null) {
			CreateUserRequest createUserRequest = new CreateUserRequest();
			createUserRequest.setUsername(response.getUsername());
			createUserRequest.setAccessToken(response.getAccessToken());
			createUserRequest.setAccessTokenSecret(response.getAccessTokenSecret());

			user = createUser(createUserRequest);
		} else {
			Token token = new Token();
			token.setAccessToken(response.getAccessToken());
			token.setAccessTokenSecret(response.getAccessTokenSecret());

			user.setToken(token);

			userRepository.save(user);
		}

		loginResponse.setToken(KeyUtils.currentTimeUUID().toString());
		loginResponse.setId(user.getId());

		TwitSecSession.getInstance().addToken(loginResponse.getToken(), user);

		return loginResponse;
	}

	@Override
	public boolean logout(BaseRequest request) {

		validate(request);

		TwitSecSession.getInstance().removeToken(request.getAuthToken());

		return true;
	}

	@Override
	public UserPreferences updateUserPreferences(UpdateUserPreferenceRequest request) {
		validate(request);
		User authenticatedUser = TwitSecSession.getInstance().getUser(request.getAuthToken());

		List<String> patterns = new ArrayList<String>();
		for(String pattern :request.getUserPreferences().getUnwantedUsernamePatterns()) {
			if(StringUtils.isNotBlank(pattern)) {
				patterns.add(pattern);
			}
		}
		request.getUserPreferences().setUnwantedUsernamePatterns(patterns);


		authenticatedUser.setPreferences(request.getUserPreferences());
		userRepository.save(authenticatedUser);

		return request.getUserPreferences();
	}

	@Override
	public User updateUserInfo(UpdateUserInfoRequest request) {
		validate(request);
		User authenticatedUser = TwitSecSession.getInstance().getUser(request.getAuthToken());

		authenticatedUser.setEmail(request.getEmail());

		return userRepository.save(authenticatedUser);
	}
}

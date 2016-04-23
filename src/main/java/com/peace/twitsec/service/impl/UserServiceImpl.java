package com.peace.twitsec.service.impl;

import com.peace.twitsec.app.common.ErrorCode;
import com.peace.twitsec.app.exception.TwitSecRuntimeException;
import com.peace.twitsec.app.util.KeyUtils;
import com.peace.twitsec.data.mongo.model.Token;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.model.UserPreferences;
import com.peace.twitsec.data.mongo.repository.UserRepository;
import com.peace.twitsec.data.session.TwitSecSession;
import com.peace.twitsec.http.request.AuthenticationRequest;
import com.peace.twitsec.http.request.CreateUserRequest;
import com.peace.twitsec.http.request.UpdateUserPreferenceRequest;
import com.peace.twitsec.http.response.LoginResponse;
import com.peace.twitsec.service.TwitSecService;
import com.peace.twitsec.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl extends TwitSecService implements UserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;


	public User findById(String id) {
		User user = userRepository.findOne(id);

		if(user == null) {
			throw new TwitSecRuntimeException(400, ErrorCode.USER_NOT_FOUND, "");
	 	}

		return user;
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
	public UserPreferences updateUserPreferences(UpdateUserPreferenceRequest request) {
		validate(request);
		User authenticatedUser = TwitSecSession.getInstance().getUser(request.getAuthToken());

		authenticatedUser.setPreferences(request.getUserPreferences());
		userRepository.save(authenticatedUser);

		return request.getUserPreferences();
	}
}

package com.peace.twitsec.service.impl;

import com.peace.twitsec.app.common.ErrorCode;
import com.peace.twitsec.app.exception.TwitSecRuntimeException;
import com.peace.twitsec.data.mongo.model.Token;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.UserRepository;
import com.peace.twitsec.http.request.CreateUserRequest;
import com.peace.twitsec.service.TwitSecService;
import com.peace.twitsec.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

		newUser.setFirstname(request.getFirstname());
		newUser.setLastname(request.getLastname());
		newUser.setPassword(request.getPassword());
		newUser.setUsername(request.getUsername());

		Token token = new Token();
		token.setAccessToken(request.getAccessToken());
		token.setAccessTokenSecret(request.getAccessTokenSecret());
		newUser.setToken(token);

		user = userRepository.save(newUser);

		return user;
	}
}

package com.peace.twitsec.service.impl;

import com.peace.twitsec.data.enums.FollowAction;
import com.peace.twitsec.data.mongo.model.AuthToken;
import com.peace.twitsec.data.mongo.model.FollowerReport;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.AuthTokenRepository;
import com.peace.twitsec.data.mongo.repository.FollowerReportRepository;
import com.peace.twitsec.service.AuthTokenService;
import com.peace.twitsec.service.FollowerReportService;
import com.peace.twitsec.service.TwitSecService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthTokenServiceImpl extends TwitSecService implements AuthTokenService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthTokenRepository authTokenRepository;


	@Override
	public AuthToken createAuthToken(AuthToken authToken) {

		return authTokenRepository.save(authToken);
	}

	@Override
	public AuthToken getAuthToken(String token) {
		return authTokenRepository.findByToken(token);
	}
}

package com.peace.twitsec.data.mongo.repository.impl;

import com.mongodb.*;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.custom.UserRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final static Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Autowired
	private MongoTemplate mongoTemplate;

    @Override
    public User loadById(Long id) {
        return null;
    }

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username).and("password").is(password));
		
		User user = mongoTemplate.findOne(query, User.class);
		
		return user;
	}

	@Override
	public User findByOauthToken(String oauthToken, String verifier) {

		Query query = new Query();
		query.addCriteria(Criteria.where("token.oauthToken").is(oauthToken).and("token.oauthTokenVerifier").is(verifier));

		User user = mongoTemplate.findOne(query, User.class);

		return user;
	}

	@Override
	public User findByUsername(String username) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		
		User user = mongoTemplate.findOne(query, User.class);
		
		return user;
	}
	

	@Override
	public User findByOneTimeToken(String oneTimeToken) {
		Query query = new Query();
		query.addCriteria(Criteria.where("oneTimeToken").is(oneTimeToken));
		
		User user = mongoTemplate.findOne(query, User.class);
		
		return user;
	}

	@Override
	public boolean deleteUser(String username) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		
		User user = mongoTemplate.findAndRemove(query, User.class);
		
		return user != null;
	}
}

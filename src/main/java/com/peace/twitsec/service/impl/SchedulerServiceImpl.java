package com.peace.twitsec.service.impl;

import com.peace.twitsec.data.mongo.model.Follower;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.UserRepository;
import com.peace.twitsec.service.SchedulerService;
import com.peace.twitsec.service.TwitSecService;
import com.peace.twitsec.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulerServiceImpl extends TwitSecService implements SchedulerService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TwitterService twitterService;

	@Override
	public void checkNewOldFollowers() {
		// scheduler edilecek tüm kullanıcıları load Et
		List<User> users = userRepository.findAll();

		for(User user : users) {
			List<Follower> followersList = new ArrayList<Follower>();

			List<Long> followerIds = twitterService.getFollowerIds(user);

			for (Long id : followerIds) {
				Follower follower = new Follower();
				follower.setTwitterId(id);

				followersList.add(follower);
			}

			user.setFollowers(followersList);

			userRepository.save(user);
		}

	}

}

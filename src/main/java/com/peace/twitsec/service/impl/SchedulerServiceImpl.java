package com.peace.twitsec.service.impl;

import com.peace.twitsec.app.util.TwitterUtil;
import com.peace.twitsec.data.enums.FollowAction;
import com.peace.twitsec.data.mongo.model.Follower;
import com.peace.twitsec.data.mongo.model.FollowerReport;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.UserRepository;
import com.peace.twitsec.service.SchedulerService;
import com.peace.twitsec.service.TwitSecService;
import com.peace.twitsec.service.FollowerReportService;
import com.peace.twitsec.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SchedulerServiceImpl extends TwitSecService implements SchedulerService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TwitterService twitterService;

	@Autowired
	private FollowerReportService twitterReportService;

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

			System.out.println("Followers for " + user.getUsername() + " :" + followersList);

			checkFollower(user, followersList);

			user.setFollowers(followersList);

			userRepository.save(user);
		}

	}

	private void checkFollower(User user, List<Follower> newFollowerList) {
		List<Follower> newFollowers = TwitterUtil.checkNewFollowers(user.getFollowers(), newFollowerList);

		List<Follower> leftFollowers = TwitterUtil.checkLeftFollowers(user.getFollowers(), newFollowerList);

		Date date = new Date();

		List<FollowerReport> followerReportList = new ArrayList<FollowerReport>();

		for(Follower newFollower: newFollowers) {
			FollowerReport report = new FollowerReport();
			report.setTwitterId(newFollower.getTwitterId());
			report.setUser(user);
			report.setCreatedAt(date);
			report.setFollowAction(FollowAction.FOLLOWED);

			followerReportList.add(report);

			System.out.println("FOLLOWED : " + report);
		}

		for(Follower leftFollower: leftFollowers) {
			FollowerReport report = new FollowerReport();
			report.setTwitterId(leftFollower.getTwitterId());
			report.setUser(user);
			report.setCreatedAt(date);
			report.setFollowAction(FollowAction.UNFOLLOWED);

			followerReportList.add(report);

			System.out.println("UNFOLLOWED : " + report);
		}

		if(followerReportList.size() > 0) {
			twitterReportService.createFollowerReports(followerReportList);
		}
	}
}
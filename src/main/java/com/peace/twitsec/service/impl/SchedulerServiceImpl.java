package com.peace.twitsec.service.impl;

import com.peace.twitsec.app.util.TwitterUtil;
import com.peace.twitsec.data.enums.FollowAction;
import com.peace.twitsec.data.mongo.model.Follower;
import com.peace.twitsec.data.mongo.model.FollowerReport;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.UserRepository;
import com.peace.twitsec.service.*;
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

	@Autowired
	private MailService mailService;

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

			try {
				//user's new followers are fetched. They will be compared with the old ones
				checkNewAndOldFollowers(user, followersList);
			} catch(Exception e) {

			}
			user.setFollowers(followersList);

			userRepository.save(user);
		}
	}

	private void checkNewAndOldFollowers(User user, List<Follower> newFollowerList) {
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

		//TODO check follower counts
		if(user.getPreferences().isSendAutoMessageToNewFollower()) {
			twitterService.sendDirectMessage(user, newFollowers, user.getPreferences().getNewFollowerAutoMessageContent());
		}


		for(Follower leftFollower: leftFollowers) {
			FollowerReport report = new FollowerReport();
			report.setTwitterId(leftFollower.getTwitterId());
			report.setUser(user);
			report.setCreatedAt(date);
			report.setFollowAction(FollowAction.UNFOLLOWED);

			followerReportList.add(report);

			System.out.println("UNFOLLOWED : " + report);


			// Notify user and mention follower name of unfollower user
			//TODO check follower counts. twitter profile needed
			System.out.println("user.getPreferences().isWarnWithEmail() : " + user.getPreferences().isWarnWithEmail());
			if(user.getPreferences().isWarnWithEmail()) {
				mailService.sendMail(user.getEmail(), "TwitSec Unfollower Notification", "Unfollowed : " + leftFollower.getTwitterId());
			}

			//TODO mention user on a tweet
			//twitter profile needed
		}

		if(followerReportList.size() > 0) {
			twitterReportService.createFollowerReports(followerReportList);
		}
	}
}

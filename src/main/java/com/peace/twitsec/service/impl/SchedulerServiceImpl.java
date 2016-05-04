package com.peace.twitsec.service.impl;

import com.peace.twitsec.app.util.TwitterUtil;
import com.peace.twitsec.data.enums.FollowAction;
import com.peace.twitsec.data.mongo.model.Follower;
import com.peace.twitsec.data.mongo.model.FollowerReport;
import com.peace.twitsec.data.mongo.model.TwitterUser;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.UserRepository;
import com.peace.twitsec.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;

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

		List<twitter4j.User> newFollowersProfiles = twitterService.getUserProfiles(user, newFollowers);
		List<TwitterUser> twitterUsers = new ArrayList<TwitterUser>();

		for(twitter4j.User twitterProfile: newFollowersProfiles) {
			//check unwanted username
			boolean hasPatternsMatched = false;
			String patternsMatched = "";
			for(String pattern : user.getPreferences().getUnwantedUsernamePatterns()) {
				if(twitterProfile.getScreenName().contains(pattern)) {
					hasPatternsMatched = true;
					patternsMatched += pattern + " ";
				}
			}
			if(hasPatternsMatched) {
				twitterService.blockUser(user, twitterProfile, patternsMatched);
				newFollowerList.remove(new Follower(twitterProfile.getId()));
			}

			if(user.getPreferences().isSendAutoMessageToNewFollower() && !hasPatternsMatched) {
				twitterService.sendDirectMessage(user, twitterProfile, user.getPreferences().getNewFollowerAutoMessageContent());
			}

			TwitterUser myTwitterUser = TwitterUtil.extractTwitterUser(twitterProfile);
			twitterUsers.add(myTwitterUser);
		}

		if(newFollowers.size() > 0) {
			twitterUsers = twitterService.createTwitterUsers(twitterUsers);
		}

		for(Follower newFollower: newFollowers) {
			FollowerReport report = new FollowerReport();
			report.setTwitterId(newFollower.getTwitterId());
			report.setUser(user);
			report.setCreatedAt(date);
			report.setFollowAction(FollowAction.FOLLOWED);
			report.setTwitterUser(getTwitterUserFromList(twitterUsers, newFollower.getTwitterId()));

			followerReportList.add(report);

			System.out.println("FOLLOWED : " + report);
		}

		if(leftFollowers.size() > 0) {
			System.out.println("user.getPreferences().isWarnWithEmail() : " + user.getPreferences().isWarnWithEmail());

			List<twitter4j.User> userProfiles = new ArrayList<twitter4j.User>();

			userProfiles = twitterService.getUserProfiles(user, leftFollowers);

			for (twitter4j.User userProfile : userProfiles) {
				TwitterUser myTwitterUser = TwitterUtil.extractTwitterUser(userProfile);
				twitterUsers.add(myTwitterUser);
			}

			if (user.getPreferences().isWarnWithEmail()) {
				String emailContent = "";
				for (twitter4j.User userProfile : userProfiles) {
					if (userProfile.getFollowersCount() >= user.getPreferences().getLeftFollowerFollowerCount()) {
						emailContent = "The user named " + userProfile.getScreenName() + " stopped following you \n";
					}
				}

				if (!emailContent.equals("")) {
					mailService.sendMail(user.getEmail(), "TwitSec Unfollower Notification", emailContent);
				}
			}

			System.out.println("user.getPreferences().isMentionOldFollowerInTweet() : " + user.getPreferences().isMentionOldFollowerInTweet());
			if (user.getPreferences().isMentionOldFollowerInTweet()) {
				for (twitter4j.User userProfile : userProfiles) {
					if (userProfile.getFollowersCount() >= user.getPreferences().getLeftFollowerFollowerCount()) {
						twitterService.tweet(user, "@" + userProfile.getScreenName() + " " + user.getPreferences().getGoodByeTweetContent());
					}
				}
			}
		}

		if(leftFollowers.size() > 0) {
			twitterUsers = twitterService.createTwitterUsers(twitterUsers);
		}

		for(Follower leftFollower: leftFollowers) {
			FollowerReport report = new FollowerReport();
			report.setTwitterId(leftFollower.getTwitterId());
			report.setUser(user);
			report.setCreatedAt(date);
			report.setFollowAction(FollowAction.UNFOLLOWED);
			report.setTwitterUser(getTwitterUserFromList(twitterUsers, leftFollower.getTwitterId()));

			followerReportList.add(report);

			System.out.println("UNFOLLOWED : " + report);
		}


		if(followerReportList.size() > 0) {
			twitterReportService.createFollowerReports(followerReportList);
		}
	}

	private TwitterUser getTwitterUserFromList(List<TwitterUser> twitterUsers, long twitterId) {
		for(TwitterUser twitterUser: twitterUsers) {
			if(twitterUser.getTwitterId() == twitterId)
				return twitterUser;
		}
		return null;
	}
}

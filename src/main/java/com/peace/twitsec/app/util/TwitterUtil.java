package com.peace.twitsec.app.util;

import com.peace.twitsec.data.mongo.model.Follower;
import com.peace.twitsec.data.mongo.model.TwitterUser;

import java.util.ArrayList;
import java.util.List;

public class TwitterUtil {

    public static List<Follower> checkLeftFollowers(List<Follower> oldFollowerList, List<Follower> newFollowerList) {

        List<Follower> leftFollowers = new ArrayList<Follower>();
        for (Follower follower : oldFollowerList) {
            if(!newFollowerList.contains(follower)) {
                leftFollowers.add(follower);
            }
        }

        return leftFollowers;
    }

    public static List<Follower> checkNewFollowers(List<Follower> oldFollowerList, List<Follower> newFollowerList) {
        List<Follower> newFollowers = new ArrayList<Follower>();

        //FIXME for the first run of the scheduler, it shouldn't recognize the followers as new
        if(oldFollowerList.size() == 0)
            return newFollowers;

        for (Follower follower : newFollowerList) {
            if(!oldFollowerList.contains(follower)) {
                newFollowers.add(follower);
            }
        }

        return newFollowers;
    }

    public static TwitterUser extractTwitterUser(twitter4j.User twitterUser) {
        TwitterUser user = new TwitterUser();

        user.setTwitterId(twitterUser.getId());
        user.setBiggerProfileImageURL(twitterUser.getBiggerProfileImageURL());
        user.setDescription(twitterUser.getDescription());
        user.setFollowersCount(twitterUser.getFollowersCount());
        user.setFriendsCount(twitterUser.getFriendsCount());
        user.setMiniProfileImageURL(twitterUser.getMiniProfileImageURL());
        user.setName(twitterUser.getName());
        user.setOriginalProfileImageURL(twitterUser.getOriginalProfileImageURL());
        user.setProfileImageURL(twitterUser.getProfileImageURL());
        user.setProfileBackgroundColor(twitterUser.getProfileBackgroundColor());
        user.setProfileBackgroundImageURL(twitterUser.getProfileBackgroundImageURL());
        user.setProfileTextColor(twitterUser.getProfileTextColor());
        user.setURL(twitterUser.getURL());
        user.setScreenName(twitterUser.getScreenName());

        return user;
    }
}

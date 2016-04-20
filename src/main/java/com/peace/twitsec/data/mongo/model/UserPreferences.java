package com.peace.twitsec.data.mongo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPreferences {

    // if the new follower's follower count is below newFollowerFollowerCount
    //default should be zero
    private Integer newFollowerFollowerCount;

    private boolean sendAutoMessageToNewFollower;

    private String newFollowerAutoMessageContent;

    // if the old follower's follower count is below oldFollowerFollowerCount
    private Integer leftFollowerFollowerCount;

    private boolean warnWithEmail;

    private boolean mentionOldFollowerInTweet;

    private String goodByeTweetContent;

}

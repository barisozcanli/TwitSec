package com.peace.twitsec.data.mongo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitterUser extends BaseEntity {

	Long twitterId;
	String screenName;
	String name;
	Integer followersCount;
	Integer friendsCount;
	String miniProfileImageURL;
	String profileImageURL;
	String biggerProfileImageURL;
	String originalProfileImageURL;
	String description;
	String profileBackgroundColor;
	String profileBackgroundImageURL;
	String profileTextColor;
	String URL;

	public TwitterUser(){
	}
}

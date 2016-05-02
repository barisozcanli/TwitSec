package com.peace.twitsec.data.mongo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.peace.twitsec.data.enums.FollowAction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "follower_reports")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowerReport extends BaseEntity{

    @DBRef
    private User user;

    @Field("follower")
    private Long twitterId;

    @DBRef
    private TwitterUser twitterUser;

    private Date createdAt;

    private FollowAction followAction;

    public FollowerReport(){
    }
}

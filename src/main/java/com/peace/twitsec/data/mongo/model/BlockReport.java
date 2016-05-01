package com.peace.twitsec.data.mongo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "blocked_reports")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockReport extends BaseEntity{

    @DBRef
    private User user;

    @Field("blocked_user")
    private Long twitterId;

    private Date createdAt;

    private String blockReason;

    public BlockReport(){
    }
}

package com.peace.twitsec.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OauthConsumerResponse {

	private String accessToken;
	private String accessTokenSecret;
	private String username;
	private String email;
}

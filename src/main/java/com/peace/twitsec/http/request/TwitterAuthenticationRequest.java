package com.peace.twitsec.http.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TwitterAuthenticationRequest {

	@NotNull
	private String oauthToken;
	
	@NotNull
	private String verifier;
}

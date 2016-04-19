package com.peace.twitsec.http.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class CreateUserRequest {

	@NotNull
	@Size(min=4)
	private String username;

	@NotNull
	private String email;

	@NotNull
	@Size(min=4)
	private String password;

	private String accessToken;

	private String accessTokenSecret;

}

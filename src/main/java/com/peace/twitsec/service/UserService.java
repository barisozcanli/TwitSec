package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.model.UserPreferences;
import com.peace.twitsec.http.request.*;
import com.peace.twitsec.http.response.LoginResponse;

public interface UserService {

    User findById(String id);

    User createUser(CreateUserRequest request);

    LoginResponse authenticate(AuthenticationRequest request);

    boolean logout(BaseRequest request);

    UserPreferences updateUserPreferences(UpdateUserPreferenceRequest request);

    LoginResponse authenticateWithTwitter(TwitterAuthenticationRequest request) throws Exception;
}

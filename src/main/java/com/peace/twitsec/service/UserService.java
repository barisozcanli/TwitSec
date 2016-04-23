package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.http.request.AuthenticationRequest;
import com.peace.twitsec.http.request.CreateUserRequest;
import com.peace.twitsec.http.response.LoginResponse;

public interface UserService {

    User findById(String id);

    User createUser(CreateUserRequest request);

    LoginResponse authenticate(AuthenticationRequest request);

}

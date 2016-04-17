package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.http.request.CreateUserRequest;

public interface UserService {

    User findById(String id);

    User createUser(CreateUserRequest request);

}

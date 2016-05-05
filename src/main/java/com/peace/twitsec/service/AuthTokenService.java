package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.AuthToken;

public interface AuthTokenService {

    AuthToken createAuthToken(AuthToken authToken);

    AuthToken getAuthToken(String token);
}

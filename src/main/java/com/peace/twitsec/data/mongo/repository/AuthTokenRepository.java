package com.peace.twitsec.data.mongo.repository;

import com.peace.twitsec.data.mongo.model.AuthToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthTokenRepository extends MongoRepository<AuthToken, String> {

    AuthToken findByToken(String token);

}

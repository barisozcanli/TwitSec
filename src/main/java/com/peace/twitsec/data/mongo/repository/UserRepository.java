package com.peace.twitsec.data.mongo.repository;

import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

    User findByUsername(String userName);

    Set<User> findByIdIn(Collection<String> ids);
    
}

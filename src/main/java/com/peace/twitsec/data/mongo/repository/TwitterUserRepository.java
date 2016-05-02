package com.peace.twitsec.data.mongo.repository;

import com.peace.twitsec.data.mongo.model.TwitterUser;
import com.peace.twitsec.data.mongo.repository.custom.TwitterUserRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.Set;

public interface TwitterUserRepository extends MongoRepository<TwitterUser, String>, TwitterUserRepositoryCustom {

    Set<TwitterUser> findByIdIn(Collection<String> ids);

    TwitterUser findByTwitterId(Long id);
}

package com.peace.twitsec.data.mongo.repository;

import com.peace.twitsec.data.mongo.model.Follower;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FollowerRepository extends MongoRepository<Follower, String> {

}

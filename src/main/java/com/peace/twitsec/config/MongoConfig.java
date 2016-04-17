package com.peace.twitsec.config;

import com.mongodb.*;
import com.peace.twitsec.app.common.AppConstants;
import com.peace.twitsec.app.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.singletonList;

@Configuration
@EnableMongoRepositories(basePackages = "com.peace.twitsec.data.mongo.repository")
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${app.profile}")
    private String profile;

    @Value("${mongodb.host}")
    private String host;

    @Value("${mongodb.port}")
    private int port;

    @Value("${mongodb.name}")
    private String db;

    @Value("${mongodb.replicaSet}")
    private String replicaSet;

    @Override
    public String getDatabaseName() {
        return db;
    }

    @Bean
    @Override
    public Mongo mongo() throws Exception {
        Mongo mongo;
        if(profile.equals(AppConstants.PROD)) {
            List<ServerAddress> serverAddresses = new LinkedList<ServerAddress>();
            for(String replica : StringUtils.splitByComma(replicaSet)) {
                serverAddresses.add(new ServerAddress(replica, port));
            }
            mongo = new MongoClient(serverAddresses);
            mongo.setReadPreference(ReadPreference.secondaryPreferred());

        } else {
            mongo = new MongoClient(singletonList(new ServerAddress(host, port)));
        }
        mongo.setWriteConcern(WriteConcern.SAFE);

        return mongo;
    }

}

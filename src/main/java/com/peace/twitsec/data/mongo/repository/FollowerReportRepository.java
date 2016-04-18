package com.peace.twitsec.data.mongo.repository;

import com.peace.twitsec.data.mongo.model.FollowerReport;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.custom.FollowerReportRepositoryCustom;
import com.peace.twitsec.data.mongo.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.Set;

public interface FollowerReportRepository extends MongoRepository<FollowerReport, String>, FollowerReportRepositoryCustom {

}

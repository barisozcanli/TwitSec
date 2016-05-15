package com.peace.twitsec.data.mongo.repository.impl;

import com.peace.twitsec.data.enums.FollowAction;
import com.peace.twitsec.data.mongo.model.FollowerReport;
import com.peace.twitsec.data.mongo.repository.custom.FollowerReportRepositoryCustom;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.List;

public class FollowerReportRepositoryImpl implements FollowerReportRepositoryCustom {

    private final static Logger logger = LoggerFactory.getLogger(FollowerReportRepositoryImpl.class);

    @Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<FollowerReport> findLatestFollowerReports(String userId, FollowAction followAction, int limit) {
		Query query = new Query();
		query.limit(limit);
		query.with(new Sort(Sort.Direction.DESC, "createdAt"));
		query.addCriteria(Criteria.where("followAction").is(followAction).and("user.$id").is(new ObjectId(userId)));
		return mongoTemplate.find(query, FollowerReport.class);
	}

	@Override
	public List<FollowerReport> findLatestFollowerReportsByDay(String userId, FollowAction followAction, int day) {

		Date date = new Date();
		date.setDate(date.getDate()-day);

		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "createdAt"));
		query.addCriteria(Criteria.where("followAction").is(followAction).and("user.$id").is(new ObjectId(userId)).and("createdAt").gt(date));
		return mongoTemplate.find(query, FollowerReport.class);
	}
}

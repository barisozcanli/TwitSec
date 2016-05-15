package com.peace.twitsec.data.mongo.repository.impl;

import com.peace.twitsec.data.mongo.model.BlockReport;
import com.peace.twitsec.data.mongo.repository.custom.BlockReportRepositoryCustom;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class BlockReportRepositoryImpl implements BlockReportRepositoryCustom {

    private final static Logger logger = LoggerFactory.getLogger(BlockReportRepositoryImpl.class);

    @Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<BlockReport> findLatestBlockReports(String userId, int limit) {
		Query query = new Query();
		query.limit(limit);
		query.with(new Sort(Sort.Direction.DESC, "createdAt"));
		query.addCriteria(Criteria.where("user.$id").is(new ObjectId(userId)));
		return mongoTemplate.find(query, BlockReport.class);
	}
}

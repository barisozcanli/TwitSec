package com.peace.twitsec.service.impl;

import com.peace.twitsec.data.enums.FollowAction;
import com.peace.twitsec.data.mongo.model.FollowerReport;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.FollowerReportRepository;
import com.peace.twitsec.service.FollowerReportService;
import com.peace.twitsec.service.TwitSecService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowerReportServiceImpl extends TwitSecService implements FollowerReportService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FollowerReportRepository followerReportRepository;

	@Override
	public List<FollowerReport> createFollowerReports(List<FollowerReport> followerReportList) {
		return followerReportRepository.save(followerReportList);
	}

	public List<FollowerReport> getFollowerReportsOfUser(User user, FollowAction followAction, Integer limit) {
		return followerReportRepository.findLatestFollowerReports(user.getId(), followAction, limit);

	}
}

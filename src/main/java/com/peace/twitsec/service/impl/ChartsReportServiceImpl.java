package com.peace.twitsec.service.impl;

import com.peace.twitsec.data.enums.FollowAction;
import com.peace.twitsec.data.mongo.model.FollowerReport;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.FollowerReportRepository;
import com.peace.twitsec.http.response.ChartsReportResponse;
import com.peace.twitsec.service.ChartsReportService;
import com.peace.twitsec.service.FollowerReportService;
import com.peace.twitsec.service.TwitSecService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChartsReportServiceImpl extends TwitSecService implements ChartsReportService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FollowerReportService followerReportService;

	//@Override
	public List<ChartsReportResponse> getChartsReportService(User authenticatedUser) {

		followerReportService.getFollowerReportsOfUser(authenticatedUser, FollowAction.FOLLOWED, 10);

		return null;

	}

}

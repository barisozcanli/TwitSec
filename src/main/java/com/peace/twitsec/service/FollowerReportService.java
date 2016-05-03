package com.peace.twitsec.service;

import com.peace.twitsec.data.enums.FollowAction;
import com.peace.twitsec.data.mongo.model.FollowerReport;
import com.peace.twitsec.data.mongo.model.User;

import java.util.List;

public interface FollowerReportService {

    List<FollowerReport> createFollowerReports(List<FollowerReport> followerReportList);

    List<FollowerReport> getFollowerReportsOfUser(User user, FollowAction followAction, Integer limit);
}

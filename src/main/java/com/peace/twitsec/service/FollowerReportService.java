package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.FollowerReport;
import com.peace.twitsec.data.mongo.model.User;

import java.util.List;

public interface FollowerReportService {

    List<FollowerReport> createFollowerReports(List<FollowerReport> followerReportList);
}

package com.peace.twitsec.service;

import com.peace.twitsec.data.enums.FollowAction;
import com.peace.twitsec.data.mongo.model.BlockReport;
import com.peace.twitsec.data.mongo.model.User;

import java.util.List;

public interface BlockReportService {

    List<BlockReport> createBlockReports(List<BlockReport> blockReportList);

    List<BlockReport> getBlockReportsOfUser(User user);

    List<BlockReport> getBlockReportsOfUser(User user, Integer limit);

    List<BlockReport> getBlockReportsOfUserByDay(User user, Integer limit);

}

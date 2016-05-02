package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.BlockReport;
import com.peace.twitsec.data.mongo.model.TwitterUser;
import com.peace.twitsec.data.mongo.model.User;

import java.util.List;

public interface BlockReportService {

    List<BlockReport> createBlockReports(List<BlockReport> blockReportList);

    List<BlockReport> getBlockReportsOfUser(User user);

}

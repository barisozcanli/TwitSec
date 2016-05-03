package com.peace.twitsec.data.mongo.repository.custom;

import com.peace.twitsec.data.enums.FollowAction;
import com.peace.twitsec.data.mongo.model.FollowerReport;
import java.util.List;

public interface FollowerReportRepositoryCustom {

    List<FollowerReport> findLatestFollowerReports(String userId, FollowAction followAction, int limit);
}

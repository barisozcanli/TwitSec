package com.peace.twitsec.data.mongo.repository.custom;

import com.peace.twitsec.data.mongo.model.BlockReport;

import java.util.List;

public interface BlockReportRepositoryCustom {

    List<BlockReport> findLatestBlockReports(String userId, int limit);
}

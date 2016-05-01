package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.BlockReport;

import java.util.List;

public interface BlockReportService {

    List<BlockReport> createBlockReports(List<BlockReport> blockReportList);
}

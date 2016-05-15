package com.peace.twitsec.service.impl;


import com.peace.twitsec.data.mongo.model.BlockReport;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.BlockReportRepository;
import com.peace.twitsec.service.BlockReportService;
import com.peace.twitsec.service.TwitSecService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockReportServiceImpl extends TwitSecService implements BlockReportService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BlockReportRepository blockReportRepository;

	@Override
	public List<BlockReport> createBlockReports(List<BlockReport> blockReportList) {
		return blockReportRepository.save(blockReportList);
	}

	public List<BlockReport> getBlockReportsOfUser(User user) {
		return blockReportRepository.findByUser(user);

	}

	@Override
	public List<BlockReport> getBlockReportsOfUser(User user, Integer limit) {
		return blockReportRepository.findLatestBlockReports(user.getId(), limit);
	}
}

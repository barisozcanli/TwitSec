package com.peace.twitsec.data.mongo.repository;

import com.peace.twitsec.data.mongo.model.BlockReport;
import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.data.mongo.repository.custom.BlockReportRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BlockReportRepository extends MongoRepository<BlockReport, String>, BlockReportRepositoryCustom {

    List<BlockReport> findByUser(User user);
}

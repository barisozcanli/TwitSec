package com.peace.twitsec.service;

import com.peace.twitsec.data.mongo.model.User;
import com.peace.twitsec.http.response.ChartsReportResponse;

import java.util.List;

public interface ChartsReportService {

    public List<ChartsReportResponse> getChartsReportService(User authenticatedUser);
}

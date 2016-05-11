package com.peace.twitsec.http.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartsReportResponse {

	List<Reports> series;
}

class Reports {
	String name;

	Hashtable<Date, Integer> data;
}
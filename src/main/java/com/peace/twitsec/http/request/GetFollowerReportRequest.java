package com.peace.twitsec.http.request;

import com.peace.twitsec.data.enums.FollowAction;
import com.peace.twitsec.data.mongo.model.UserPreferences;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GetFollowerReportRequest extends BaseRequest{

	FollowAction followAction;
	Integer limit;
}

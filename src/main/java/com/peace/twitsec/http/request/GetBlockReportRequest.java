package com.peace.twitsec.http.request;

import com.peace.twitsec.data.enums.FollowAction;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class GetBlockReportRequest extends BaseRequest{

	Integer limit;
}

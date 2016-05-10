package com.peace.twitsec.http.request;

import com.peace.twitsec.data.mongo.model.UserPreferences;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateUserInfoRequest extends BaseRequest{

	String email;

}

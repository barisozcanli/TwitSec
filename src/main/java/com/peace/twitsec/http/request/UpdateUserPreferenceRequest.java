package com.peace.twitsec.http.request;

import com.peace.twitsec.data.mongo.model.UserPreferences;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateUserPreferenceRequest extends BaseRequest{

	UserPreferences userPreferences;

}

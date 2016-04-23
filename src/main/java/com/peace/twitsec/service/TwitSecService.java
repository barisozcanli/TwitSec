package com.peace.twitsec.service;

import com.peace.twitsec.app.common.ErrorCode;
import com.peace.twitsec.app.exception.TwitSecRuntimeException;
import com.peace.twitsec.app.exception.TwitSecValidationException;
import com.peace.twitsec.data.session.TwitSecSession;
import com.peace.twitsec.http.request.BaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

public abstract class TwitSecService {

    @Autowired
    protected Validator validator;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    protected void validateFields(Object request) throws TwitSecValidationException {
        Set<? extends ConstraintViolation<?>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() > 0) {
            throw new TwitSecValidationException(constraintViolations);
        }
    }
    
    protected void validate(BaseRequest request) throws TwitSecRuntimeException{
    	
    	if(request.getAuthToken() == null || "".equalsIgnoreCase(request.getAuthToken())){
    		throw new TwitSecRuntimeException(400, ErrorCode.INVALID_INPUT, "Authentication token field is empty", "");
    	}
    	if (!TwitSecSession.getInstance().validateToken(request.getAuthToken())) {
    		throw new TwitSecRuntimeException(400, ErrorCode.OPERATION_NOT_ALLOWED, "");
		}
    	
    	validateFields(request);
    }

    protected void validate(String authToken) throws TwitSecRuntimeException{

        BaseRequest request = new BaseRequest();
        request.setAuthToken(authToken);

        validate(request);
    }

}

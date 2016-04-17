package com.peace.twitsec.app.exception;

//import com.peace.twitsec.app.bundle.TwitSecErrorBundle;
import com.peace.twitsec.app.response.ErrorResponse;

public class TwitSecException extends RuntimeException {

    private String key;

    public TwitSecException(String key){
        this.key = key;
    }

    public ErrorResponse getErrorResponse() {
        ErrorResponse response = new ErrorResponse();
        //response.setErrorCode(TwitSecErrorBundle.getErrorCode(key));
        //response.setApplicationMessage(TwitSecErrorBundle.getApplicationErrorMessage(key));
        //response.setConsumerMessage(TwitSecErrorBundle.getConsumerErrorMessage(key));
        return response;
    }
}

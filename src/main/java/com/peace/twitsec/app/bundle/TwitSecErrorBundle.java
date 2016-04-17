package com.peace.twitsec.app.bundle;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

//@Component
//@Scope("singleton")
public class TwitSecErrorBundle {

    private static final ResourceBundle ERROR_BUNDLE = ResourceBundle.getBundle("error-messages");
    private static final String APPLICATION_MESSAGE = "app.";
    private static final String CONSUMER_MESSAGE = "con.";
    private static final String ERROR_CODE = "code.";
    private static final String HTTP_STATUS = "http.";

    public static String getApplicationErrorMessage(String key) {
        return getMessage(APPLICATION_MESSAGE + key);
    }

    public static String getConsumerErrorMessage(String key) {
        return getMessage(CONSUMER_MESSAGE + key);
    }

    public static String getErrorCode(String key) {
        return getMessage(ERROR_CODE + key);
    }

    public static String getHttpStatus(String key) {
        return getMessage(HTTP_STATUS + key);
    }

    private static String getMessage(String key) {

        try {
            String s = ERROR_BUNDLE.getString(key);
            return s;
        }
        catch (Exception e) {
            return "Unresolved key: " + key;
        }
    }


}

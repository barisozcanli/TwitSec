package com.peace.twitsec.app.response;

import java.io.Serializable;

public class Response implements Serializable {

    protected String status;

    public Response(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

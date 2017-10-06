package com.sap.cloud.lm.sl.common.util;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

public class ResponseRenderer {

    public static Response renderResponseForStatus(Status status, String message) {
        return renderResponseForStatus(Response.status(status), message);
    }

    public static Response renderResponseForStatus(int status, String message) {
        return renderResponseForStatus(Response.status(status), message);
    }

    private static Response renderResponseForStatus(ResponseBuilder builder, String message) {
        if (message != null) {
            builder.type("text/plain");
            builder.entity(message);
        }
        return builder.build();
    }
}

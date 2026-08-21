package com.showroom.common;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class ResponseUtil {

    private final static String origin = "http://localhost:5173";

    private ResponseUtil() {
    }

    public static APIGatewayProxyResponseEvent jsonify(int statusCode, String body) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(Map.of(
                    "Content-Type", "application/json",
                    "Access-Control-Allow-Origin", origin,
                    "Access-Control-Allow-Headers",
                    "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token,X-Amz-User-Agent",
                    "Access-Control-Allow-Methods",
                    "OPTIONS,GET,POST,PUT,DELETE"
            ))
                .withBody(body);
    }
}

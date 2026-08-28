package com.showroom.common;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.LinkedHashMap;

public class JsonLogger {
    private final Context context;
    private static final ObjectMapper mapper = new ObjectMapper();

    public JsonLogger(Context context) {
        this.context = context;
    }

    private void log(String level,String message, Map<String, Object> additionalFields) {
        try {
            Map<String, Object> logEntry = new LinkedHashMap<>();
            logEntry.put("level", level);
            logEntry.put("message", message);
            logEntry.put("requestId", context.getAwsRequestId());
            logEntry.putAll(additionalFields);

            String jsonLog = mapper.writeValueAsString(logEntry);
            System.out.println(jsonLog);
        } catch (Exception e) {
            System.err.println("Failed to log message: " + e.getMessage());
        }
    }

    public void info(String message, Map<String, Object> additionalFields) {
        log("INFO", message, additionalFields);
    }
    
    public void error(String message, Map<String, Object> additionalFields) {
        log("ERROR", message, additionalFields);
    }
}

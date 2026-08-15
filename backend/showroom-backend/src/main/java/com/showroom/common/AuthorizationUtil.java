package com.showroom.common;

import java.util.*;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

public class AuthorizationUtil {
    private AuthorizationUtil() {
    }

    public static Map<String, Object> getClaims(APIGatewayProxyRequestEvent event) {
        if (event == null || event.getRequestContext() == null || event.getRequestContext().getAuthorizer() == null) {
            return Collections.emptyMap();
        }

        Map<String, Object> authorizer = event.getRequestContext().getAuthorizer();

        Object claimsObject = authorizer.get("claims");

        if (!(claimsObject instanceof Map)) {
            return Collections.emptyMap();
        }

        Map<?, ?> rawClaims = (Map<?, ?>) claimsObject;
        Map<String, Object> claims = new HashMap<>();

        rawClaims.forEach((key, value) -> 
            claims.put((String) key, value)
            
        );
        return claims;
    }

    public static Set<String> getGroups(APIGatewayProxyRequestEvent event) {
        Map<String, Object> claims = getClaims(event);
        Object groupsObject = claims.get("cognito:groups");

        if (groupsObject == null) {
            return Collections.emptySet();
        }
        if (groupsObject instanceof String groupString) {
            return new HashSet<>( Arrays.asList(groupString.split(",")));
        } else
        if (groupsObject instanceof List<?> groupList) {
            Set<String> result = new HashSet<>();
            for (Object group : groupList) {
                if (group != null) {
                    result.add(group.toString());
                }
            }
            return result;
        }

        return Collections.emptySet();
    }

    public static Set<String> getUserId(APIGatewayProxyRequestEvent event) {
        Map<String, Object> claims = getClaims(event);
        Object userIdObject = claims.get("sub");

        if (userIdObject == null) {
            return Collections.emptySet();
        }
        if (userIdObject instanceof String userIdString) {
            return new HashSet<>(Arrays.asList(userIdString));
        }

        return Collections.emptySet();
    }

    public static boolean isAdmin(APIGatewayProxyRequestEvent event) {
        Set<String> groups = getGroups(event);
        return groups.contains("ADMIN");
    }
}

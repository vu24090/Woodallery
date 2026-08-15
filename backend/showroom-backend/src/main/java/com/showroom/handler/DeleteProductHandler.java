package com.showroom.handler;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.showroom.common.ApiResponse;
import com.showroom.common.AuthorizationUtil;
import com.showroom.common.JsonLogger;
import com.showroom.common.ResponseUtil;
import com.showroom.repository.ProductRepository;
import com.showroom.service.ProductService;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;

public class DeleteProductHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ProductService productService = new ProductService(new ProductRepository());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private JsonLogger logger;
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        long startTime = System.currentTimeMillis();
        logger = new JsonLogger(context);
        try {
            logger.info(
            "DELETE_PRODUCT_REQUEST_RECEIVED",
            Map.of(
                "path", event.getPath(),
                "method", event.getHttpMethod()
            )
        );

        APIGatewayProxyResponseEvent result =
                deleteProduct(event, startTime);

        logger.info(
            "DELETE_PRODUCT_REQUEST_COMPLETED",
            Map.of(
                "status", result.getStatusCode(),
                "duration", System.currentTimeMillis() - startTime
            )
        );

        return result;
        }
        catch (Exception e) {
            logger.error(
                "DELETE_PRODUCT_UNEXPECTED_ERROR",
                Map.of(
                    "error",
                    e.getMessage() == null ? "" : e.getMessage(),
                    "duration",
                    System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(500, "{\"error\": \"Internal Server Error\"}");
        }
    }
    
    private APIGatewayProxyResponseEvent deleteProduct(APIGatewayProxyRequestEvent event, long startTime) throws JsonProcessingException {
        if (!AuthorizationUtil.isAdmin(event)) {
            logger.error(
                "DELETE_PRODUCT_FORBIDDEN",
                Map.of(
                    "reason", "Admin access required",
                    "duration",
                    System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(403, objectMapper.writeValueAsString(ApiResponse.error("Forbidden: Admin access required")));
        }
        logger.info(
            "USER_AUTHORIZED",
            Map.of(
                "duration",
                System.currentTimeMillis() - startTime
            )
        );
        String id = event.getPathParameters().get("productId");
        logger.info(
            "PRODUCT_ID_EXTRACTED",
            Map.of(
                "productId",
                id == null ? "" : id
            )
        );
        if (id == null || id.isEmpty()) {
            logger.error(
                "DELETE_PRODUCT_MISSING_ID",
                Map.of(
                    "duration",
                    System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(400, objectMapper.writeValueAsString(ApiResponse.error("Product ID is required")));
        }
        logger.info(
            "DELETING_PRODUCT_FROM_DYNAMODB",
            Map.of(
                "productId", id
            )
        );
        boolean isDeleted = productService.deleteProduct(id);

        if (isDeleted) {
            logger.info(
                "DELETE_PRODUCT_SUCCESS",
                Map.of(
                    "productId", id,
                    "duration",
                    System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(204, objectMapper.writeValueAsString(ApiResponse.success("Product deleted successfully", null)));
        } else {
            logger.error(
                "DELETE_PRODUCT_NOT_FOUND",
                Map.of(
                    "productId", id,
                    "duration",
                    System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(404, objectMapper.writeValueAsString(ApiResponse.error("Product not found")));
        }
    }
}
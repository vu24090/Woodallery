package com.showroom.handler;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.showroom.common.ApiResponse;
import com.showroom.common.AuthorizationUtil;
import com.showroom.common.JsonLogger;
import com.showroom.common.JsonValidator;
import com.showroom.common.ResponseUtil;
import com.showroom.model.Product;
import com.showroom.repository.ProductRepository;
import com.showroom.service.ProductService;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;

public class UpdateProductHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ProductService productService = new ProductService(new ProductRepository());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private JsonLogger logger;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        long startTime = System.currentTimeMillis();
        logger = new JsonLogger(context);
        try {
            logger.info(
                "UPDATE_PRODUCT_REQUEST_RECEIVED",
                Map.of(
                    "path", event.getPath(),
                    "method", event.getHttpMethod()
                )
            );
            APIGatewayProxyResponseEvent result = updateProduct(event, startTime);

            logger.info(
                "UPDATE_PRODUCT_REQUEST_COMPLETED",
                Map.of(
                    "status", result.getStatusCode(),
                    "duration", System.currentTimeMillis() - startTime
                )
            );
    
            return result;
        } catch (Exception e) {
            logger.error(
                "UPDATE_PRODUCT_UNEXPECTED_ERROR",
                Map.of(
                    "error", e.getMessage() == null ? "" : e.getMessage(),
                    "duration", System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(500, "{\"error\": \"Internal Server Error\"}");
        }
        
    }

    private APIGatewayProxyResponseEvent updateProduct(APIGatewayProxyRequestEvent event, long startTime) throws JsonProcessingException {
        logger.info(
            "UPDATE_PRODUCT_FUNCTION_STARTED",
            Map.of()
        );
        if (!AuthorizationUtil.isAdmin(event)) {
            logger.error(
                "UPDATE_PRODUCT_FORBIDDEN",
                Map.of(
                    "reason", "Admin access required",
                    "duration", System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(403, objectMapper.writeValueAsString(ApiResponse.error("Forbidden: Admin access required")));
        }
        logger.info(
            "USER_AUTHORIZED",
            Map.of(
                "duration", System.currentTimeMillis() - startTime
            )
        );

        Map<String, String> pathParameters = event.getPathParameters();
        String id = pathParameters != null ? pathParameters.get("productId") : null;

        logger.info(
            "PRODUCT_ID_EXTRACTED",
            Map.of(
                "productId", id == null ? "" : id
            )
        );

        if (id == null || id.isEmpty()) {
            logger.error(
                "UPDATE_PRODUCT_MISSING_ID",
                Map.of(
                    "duration",
                    System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(400, objectMapper.writeValueAsString(ApiResponse.error("Product ID is required")));
        }

        Product product = objectMapper.readValue(event.getBody(), Product.class);

        logger.info(
            "PRODUCT_PAYLOAD_VALIDATING",
            Map.of(
                "productId", id
            )
        );

        String validationError = JsonValidator.validate(product);

        if (validationError != null) {
            logger.error(
                "UPDATE_PRODUCT_VALIDATION_FAILED",
                Map.of(
                    "productId", id,
                    "validationError", validationError,
                    "duration",
                    System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(400, objectMapper.writeValueAsString(ApiResponse.error(validationError)));
        }
        logger.info(
            "UPDATE_PRODUCT_VALIDATION_SUCCESS",
            Map.of(
                "productId", id,
                "duration",
                System.currentTimeMillis() - startTime
            )
        );
    
        logger.info(
            "UPDATING_PRODUCT_IN_DYNAMODB",
            Map.of(
                "productId", id
            )
        );
        Product updatedProduct = productService.updateProduct(id, product);

        logger.info(
            "UPDATE_PRODUCT_SUCCESS",
            Map.of(
                "productId", updatedProduct.getProductId(),
                "name", updatedProduct.getName(),
                "slug", updatedProduct.getSlug(),
                "duration",
                System.currentTimeMillis() - startTime
            )
        );
        return ResponseUtil.jsonify(200, objectMapper.writeValueAsString(ApiResponse.success("Product updated successfully", updatedProduct)));
    }
}

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

public class CreateProductHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ProductService productService = new ProductService(new ProductRepository());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private JsonLogger logger;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        long startTime = System.currentTimeMillis();
        logger = new JsonLogger(context);
        try {
            logger.info(
                "CREATE_PRODUCT_REQUEST_RECEIVED",
                Map.of(
                        "path", event.getPath(),
                        "method", event.getHttpMethod()
                )
            );

            APIGatewayProxyResponseEvent result = createProduct(event, startTime);

            logger.info(
                "CREATE_PRODUCT_REQUEST_COMPLETED",
                Map.of(
                        "status", result.getStatusCode(),
                        "duration", System.currentTimeMillis() - startTime
                )
            );
            return result;

        } catch (JsonProcessingException e) {
            logger.error(
                "CREATE_PRODUCT_INVALID_JSON",
                Map.of(
                        "error", e.getMessage() == null ? "" : e.getMessage(),
                        "duration", System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(400, "{\"error\": \"Invalid JSON format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(
                "CREATE_PRODUCT_UNEXPECTED_ERROR",
                Map.of(
                        "error", e.getMessage() == null ? "" : e.getMessage(),
                        "duration", System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(500, "{\"error\": \"Internal Server Error\"}");
        }
        
    }

    private APIGatewayProxyResponseEvent createProduct(APIGatewayProxyRequestEvent event, long startTime) throws JsonProcessingException {
        logger.info("CREATE_PRODUCT_FUNCTION_STARTED", Map.of());
        if (!AuthorizationUtil.isAdmin(event)) {
            logger.error(
                "CREATE_PRODUCT_FORBIDDEN",
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
        Product product = objectMapper.readValue(event.getBody(), Product.class);
        logger.info(
            "PRODUCT_PAYLOAD_VALIDATING",
            Map.of()
        );
        String validationError = JsonValidator.validate(product);
        if (validationError != null) {
            logger.error(
                "CREATE_PRODUCT_VALIDATION_FAILED",
                Map.of(
                        "validationError", validationError,
                        "duration", System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(400,objectMapper.writeValueAsString(ApiResponse.error(validationError)));
        }
        logger.info(
            "CREATE_PRODUCT_VALIDATION_SUCCESS",
            Map.of(
                    "duration", System.currentTimeMillis() - startTime
            )
        );
        logger.info(
            "SAVING_PRODUCT_TO_DYNAMODB",
            Map.of(
            )
        );
        Product createdProduct = productService.createProduct(product);
        logger.info(
            "CREATE_PRODUCT_SUCCESS",
            Map.of(
                    "productId", createdProduct.getProductId(),
                    "name", createdProduct.getName(),
                    "slug", createdProduct.getSlug(),
                    "duration", System.currentTimeMillis() - startTime
            )
        );
        return ResponseUtil.jsonify(201, objectMapper.writeValueAsString(ApiResponse.success("Product created successfully", createdProduct)));
    }
}

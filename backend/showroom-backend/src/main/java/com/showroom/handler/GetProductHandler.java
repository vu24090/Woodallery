package com.showroom.handler;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.showroom.common.ApiResponse;
import com.showroom.common.JsonLogger;
import com.showroom.common.ResponseUtil;
import com.showroom.model.Product;
import com.showroom.repository.ProductRepository;
import com.showroom.service.ProductService;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;

public class GetProductHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ProductService productService = new ProductService(new ProductRepository());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private JsonLogger logger;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        long startTime = System.currentTimeMillis();
        logger = new JsonLogger(context);
        try {
            logger.info(
                "GET_PRODUCT_BY_ID_REQUEST_RECEIVED",
                Map.of(
                        "path", event.getPath(),
                        "method", event.getHttpMethod()
                )
            );
            APIGatewayProxyResponseEvent result = getProduct(event, startTime);
            logger.info(
                "GET_PRODUCT_BY_ID_REQUEST_COMPLETED",
                Map.of(
                        "status", result.getStatusCode(),
                        "duration", System.currentTimeMillis() - startTime
                )
            );
            return result;
        } catch (Exception e) {
            logger.error(
                "GETTING_PRODUCT_UNEXPECTED_ERROR",
                Map.of(
                        "error", e.getMessage() == null ? "" : e.getMessage(),
                        "duration", System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(500, "{\"error\": \"Error processing request\"}");
        }
        
    }

    private APIGatewayProxyResponseEvent getProduct(APIGatewayProxyRequestEvent event, long startTime) throws JsonProcessingException {
        logger.info("GETTING_PRODUCT_BY_ID_FUNCTION_STARTED", Map.of());
        String id = event.getPathParameters().get("productId");
        logger.info("GET_PRODUCT_PARAMETERS_PARSED",
            Map.of(
                "productsID", id == null ? "" : id
            )
        );
        if (id == null || id.isEmpty()) {
            logger.error("PRODUCT_ID_NOT_FOUND",
                Map.of("duration", System.currentTimeMillis() - startTime)
            );
            return ResponseUtil.jsonify(400, objectMapper.writeValueAsString(ApiResponse.error("Product ID is required")));
        }
        logger.info("SEARCHING_CURRENT_PRODUCT_WITH_MATCHING_ID", 
            Map.of(
                "withID", id
            )
        );
        Product product = productService.getProductById(id);
        if (product == null) {
            logger.info("SEARCH_FAILED", 
            Map.of(
                "there is no product found with id ", id,
                "duration", System.currentTimeMillis() - startTime
            )
            );
            return ResponseUtil.jsonify(404, objectMapper.writeValueAsString(ApiResponse.error("Product not found")));
        }
        logger.info("GET_PRODUCT_FUNCTION_SUCCESSED", 
            Map.of(
                "category", product.getCategory(),
                "name", product.getName(),
                "duration", System.currentTimeMillis() - startTime
            )
        );
        return ResponseUtil.jsonify(200, objectMapper.writeValueAsString(product));
    }
}
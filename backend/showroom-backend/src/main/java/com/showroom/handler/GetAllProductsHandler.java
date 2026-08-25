package com.showroom.handler;

import java.util.List;
import java.util.Map;

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
import com.amazonaws.services.lambda.runtime.Context;

public class GetAllProductsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ProductService productService = new ProductService(new ProductRepository());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private JsonLogger logger;

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        long startTime = System.currentTimeMillis();
        logger = new JsonLogger(context);
        try {
            logger.info(
                "GET_PRODUCTS_REQUEST_RECEIVED",
                Map.of(
                        "path", event.getPath(),
                        "method", event.getHttpMethod()
                )
            );
            Map<String, String> params =event.getQueryStringParameters();
            String category = params != null ? params.get("category") : null;
            String keyword = params != null ? params.get("keyword") : null;
            logger.info(
                "PARAMS_CHECKED",
                Map.of(
                    "category", category == null ? "" : category,
                    "keyword", keyword == null ? "" : keyword,
                    "duration", System.currentTimeMillis() - startTime
                )
            );
            if (category != null && !category.isEmpty()) {
                logger.info(
                    "FILLING_PRODUCTS_REQUEST_RECEIVED",
                    Map.of(
                            "category", category == null ? "" : category,
                            "keyword", keyword == null ? "" : keyword
                    )
                );
                APIGatewayProxyResponseEvent result = getProductByCategoryName(category, keyword, startTime);
                logger.info(
                    "FILLING_PRODUCTS_REQUEST_COMPLETED",
                    Map.of(
                            "category", category == null ? "" : category,
                            "keyword", keyword == null ? "" : keyword,
                            "duration", System.currentTimeMillis() - startTime
                    )
                );
                return result;
            }
            logger.info(
                "GETTING_PRODUCTS_REQUEST_RECEIVED", null
            );

            APIGatewayProxyResponseEvent result = getAllProducts(startTime);

            logger.info(
                "GETTING_PRODUCTS_REQUEST_COMPLETED",
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

    private APIGatewayProxyResponseEvent getAllProducts(long startTime) throws JsonProcessingException {
        logger.info("GETTING_PRODUCTS_FUNCTION_STARTED", Map.of());
        List<Product> products = productService.getAllProducts();
        logger.info("PRODUCT_GETTING_SERVICE_COMPLETED",
            Map.of(
                "duration",System.currentTimeMillis() - startTime
            )
        );
        try {
            String responseBody = objectMapper.writeValueAsString(products);
            logger.info(
                "GETTING_PRODUCTS_SUCCESSED",
                Map.of(
                    "duration",System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(200, responseBody);
        } catch (Exception e) {
            logger.error(
                "GETTING_PRODUCTS_FAILED",
                Map.of(
                    "error", e.getMessage() == null ? "" : e.getMessage(),
                    "duration", System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(500, objectMapper.writeValueAsString(ApiResponse.error("Error processing products")));
        }
    }

    private APIGatewayProxyResponseEvent getProductByCategoryName(String category , String keyword, long startTime) throws JsonProcessingException {
        logger.info("GETTING_PRODUCTS_FUNCTION_STARTED", Map.of() );
        List<Product> products = productService.getProductByCategoryKeyword(category,keyword);
        logger.info("PRODUCT_GETTING_SERVICE_COMPLETED",
            Map.of(
                "duration",System.currentTimeMillis() - startTime
            )
        );
        try {
            String responseBody = objectMapper.writeValueAsString(products);
            logger.info(
                "GETTING_PRODUCTS_SUCCESSED",
                Map.of(
                    "duration",System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(200, responseBody);
        } catch (Exception e) {
            logger.error(
                "GETTING_PRODUCTS_FAILED",
                Map.of(
                    "error", e.getMessage() == null ? "" : e.getMessage(),
                    "duration", System.currentTimeMillis() - startTime
                )
            );
            return ResponseUtil.jsonify(500, objectMapper.writeValueAsString(ApiResponse.error("Error processing products")));
        }
    }
}

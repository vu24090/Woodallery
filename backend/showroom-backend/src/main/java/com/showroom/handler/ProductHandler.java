package com.showroom.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.showroom.service.ProductService;
import com.showroom.common.ApiResponse;
import com.showroom.common.ProductValidator;
import com.showroom.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

public class ProductHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ProductService productService = new ProductService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String method = event.getHttpMethod();
        String path = event.getPath();
        try {

            if ("GET".equals(method) && path.equals("/products")) {
                Map<String, String> queryParams = event.getQueryStringParameters();
                String name = queryParams != null ? queryParams.get("name") : null;
                if (name != null && !name.isEmpty()) {
                    return getProductsByName(name);
                }
                return getAllProducts();
            }

            if ("GET".equals(method) && path.matches("/products/[^/]+")) {
                String id = path.substring(path.lastIndexOf("/") + 1);
                return getProduct(id);
            }

            if ("POST".equals(method) && path.equals("/products")) {
                return createProduct(event);
            }

            if ("PUT".equals(method) && path.matches("/products/[^/]+")) {
                String id = path.substring(path.lastIndexOf("/") + 1);
                return updateProduct(id, event);
            }

            if ("DELETE".equals(method) && path.matches("/products/[^/]+")) {
                String id = path.substring(path.lastIndexOf("/") + 1);
                return deleteProduct(id);
            }

            return response(
                    404,
                    objectMapper.writeValueAsString(ApiResponse.error("Not Found"))
            );

        } catch (Exception e) {


            return response(
                    500,"{\"message\":\"Internal Server Error\"}"
            );
        }
    }

    private APIGatewayProxyResponseEvent getAllProducts() throws JsonProcessingException {
        List<Product> products = productService.getAllProducts();
        try {
            String responseBody = objectMapper.writeValueAsString(products);
            return response(200, responseBody);
        } catch (Exception e) {
            return response(500, objectMapper.writeValueAsString(ApiResponse.error("Error processing products")));
        }
    }

    private APIGatewayProxyResponseEvent getProduct(String id) throws JsonProcessingException {
        Product product = productService.getProductById(id);
    
        if (product == null) {
            return response(404, objectMapper.writeValueAsString(ApiResponse.error("Product not found")));
        }
    
        try {
            return response(200, objectMapper.writeValueAsString(product));
    
        } catch (Exception e) {
            e.printStackTrace();
            return response(500, "{\"message\":\"Error processing product\"}");
        }
    }

    private APIGatewayProxyResponseEvent createProduct(APIGatewayProxyRequestEvent event) throws JsonProcessingException {
            Product product = objectMapper.readValue(event.getBody(), Product.class);
            String validationError = ProductValidator.validate(product);
            if (validationError != null) {
                return response(400,objectMapper.writeValueAsString(ApiResponse.error(validationError)));
            }
            Product createdProduct = productService.createProduct(product);
            return response(201, objectMapper.writeValueAsString(ApiResponse.success("Product created successfully", createdProduct)));
    }

    private APIGatewayProxyResponseEvent deleteProduct(String id) throws JsonProcessingException {
        boolean product = productService.deleteProduct(id);
        if (product) {
            return response(204, objectMapper.writeValueAsString(ApiResponse.success("Product deleted successfully", product)));
        } else {
            return response(404, objectMapper.writeValueAsString(ApiResponse.error("Product not found")));
        }
    }

    private APIGatewayProxyResponseEvent getProductsByName(String name) throws JsonProcessingException {
        List<Product> products = productService.getProductsByName(name);
        try {
            String responseBody = objectMapper.writeValueAsString(products);
            return response(200, responseBody);
        } catch (Exception e) {
            return response(500, objectMapper.writeValueAsString(ApiResponse.error("Error processing products by name")));
        }
    }

    private APIGatewayProxyResponseEvent updateProduct(String id, APIGatewayProxyRequestEvent event) throws JsonProcessingException {
        Product product = objectMapper.readValue(event.getBody(), Product.class);
        String validationError = ProductValidator.validate(product);
        if (validationError != null) {
            return response(400, objectMapper.writeValueAsString(ApiResponse.error(validationError)));
        }
        Product updatedProduct = productService.updateProduct(id, product);
        return response(200, objectMapper.writeValueAsString(ApiResponse.success("Product updated successfully", updatedProduct)));
    }


    private APIGatewayProxyResponseEvent response(int statusCode, String body) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(body);
    }

}

package com.showroom.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.showroom.service.ProductService;
import com.showroom.model.Product;
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
                return getAllProducts();
            }

            if ("GET".equals(method) && path.matches("/products/[^/]+")) {
                String id =
                        path.substring(path.lastIndexOf("/") + 1);
                return getProduct(id);
            }

            if ("POST".equals(method) && path.equals("/products")) {
                return createProduct(event);
            }

            if ("DELETE".equals(method) && path.matches("/products/[^/]+")) {
                String id = path.substring(path.lastIndexOf("/") + 1);
                return deleteProduct(id);
            }

            return response(
                    404,
                    "{\"message\":\"Route not found\"}"
            );

        } catch (Exception e) {

            e.printStackTrace();

            return response(
                    500,
                    "{\"message\":\"Internal Server Error\"}"
            );
        }
    }

    private APIGatewayProxyResponseEvent getAllProducts() {
        List<Product> products = productService.getAllProducts();
        try {
            String responseBody = objectMapper.writeValueAsString(products);
            return response(200, responseBody);
        } catch (Exception e) {
            return response(500, "{\"message\":\"Error processing products\"}");
        }
    }

    private APIGatewayProxyResponseEvent getProduct(String slug) {
        Product product = productService.getProductBySlug(slug);
    
        if (product == null) {
            return response(404, "{\"message\":\"Product not found\"}");
        }
    
        try {
            ObjectMapper objectMapper = new ObjectMapper();
    
            String body = objectMapper.writeValueAsString(product);
    
            return response(200, body);
    
        } catch (Exception e) {
            e.printStackTrace();
            return response(500, "{\"message\":\"Error processing product\"}");
        }
    }

    private APIGatewayProxyResponseEvent createProduct(APIGatewayProxyRequestEvent event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(event.getBody(), Product.class);
            Product createdProduct = productService.createProduct(product);
            String responseBody = objectMapper.writeValueAsString(createdProduct);
            return response(201, responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return response(500, "{\"message\":\"Error creating product\"}");
        }
    }

    private APIGatewayProxyResponseEvent deleteProduct(String id) {
        boolean product = productService.deleteProduct(id);
        if (product) {
            return response(204, "");
        } else {
            return response(404, "{\"message\":\"Product not found\"}");
        }
    }

    private APIGatewayProxyResponseEvent response(int statusCode, String body) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(body);
    }
}

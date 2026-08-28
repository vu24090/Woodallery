package com.showroom.service;

import com.showroom.model.Product;
import com.showroom.repository.ProductRepository;

import java.time.Instant;
import java.util.*;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public Product getProductById(String id) {
        Product product = productRepository.getProductById(id);
        if (product == null) {
            return null;
        }
        return product;
    }

    public Product createProduct(Product product) {
        String id = UUID.randomUUID().toString();
        String slug = product.getName().toLowerCase().replace(" ", "-");
        product.setSlug(slug);
        product.setProductId(id);
        product.setCreateAt(Instant.now().toString());
        productRepository.saveProduct(product);
        return product;
    }

    public Product updateProduct(String id, Product product) {
        Product existingProduct = productRepository.getProductById(id);
        if (existingProduct == null) {
            return null;
        }
        product.setProductId(existingProduct.getProductId());
        product.setSlug(existingProduct.getSlug());
        product.setCreateAt(existingProduct.getCreateAt());
        productRepository.saveProduct(product);
        return product;
    }


    public boolean deleteProduct(String id) {
        Product existingProduct = productRepository.getProductById(id);
        if (existingProduct == null) {
            return false;
        }
        productRepository.deleteProduct(id);
        return true;
    }

    public List<Product> getProductByCategoryKeyword(String category, String keyword) {
        List<Product> products = productRepository.getProductByCategoryName(category);

        if (keyword == null || keyword.isBlank()) {
            return products;
        }

        String keywordLower = keyword.toLowerCase();

        return products.stream()
                .filter(product ->
                        product.getName() != null &&
                        product.getName()
                                .toLowerCase()
                                .contains(keywordLower))
                .toList();
    }
}

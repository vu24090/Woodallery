package com.showroom.service;

import com.showroom.exception.ProductNotFoundException;
import com.showroom.model.Product;
import com.showroom.repository.ProductRepository;

import java.time.Instant;
import java.util.*;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepository();
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public Product getProductById(String id) {
        Product product = productRepository.getProductById(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    public Product createProduct(Product product) {
        String id = UUID.randomUUID().toString();
        product.setProductId(id);
        product.setCreateAt(Instant.now().toString());
        productRepository.saveProduct(product);
        return product;
    }

    public Product updateProduct(String id, Product product) {
        Product existingProduct = productRepository.getProductById(id);
        if (existingProduct == null) {
            throw new ProductNotFoundException(id);
        }
        product.setProductId(existingProduct.getProductId());
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

    public List<Product> getProductsByName(String name) {
        return productRepository.getProductsByName(name);
    }
}

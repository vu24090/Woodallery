package com.showroom.service;

import com.showroom.model.Product;
import com.showroom.repository.ProductRepository;

import java.util.*;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepository();
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public Product getProductBySlug(String slug) {
        return productRepository.getProductById(slug);
    }

    public Product createProduct(Product product) {
        productRepository.saveProduct(product);
        return product;
    }

    public Product updateProduct(String id, Product product) {
        Product existingProduct = productRepository.getProductById(id);
        if (existingProduct == null) {
            return null;
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
}

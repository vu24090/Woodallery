package com.showroom.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

@DynamoDbBean
public class Product {
    private String productId;
    private String category;
    private String name;
    private String slug;
    private String description;
    private String material;
    private String color;
    private String dimensions;
    private String createAt;
    private String imageUrl;

    public Product() {
    }

    public Product(String productId, String category, String name, String slug, String description, String material, String color, String dimensions, String createAt, String imageUrl) {
        this.productId = productId;
        this.category = category;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.material = material;
        this.color = color;
        this.dimensions = dimensions;
        this.createAt = createAt;
        this.imageUrl = imageUrl;
    }

    @DynamoDbPartitionKey
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "CategoryWithNameIndex")
    @NotBlank(message = "Category is required")
    @Size(min = 1, max = 100, message = "Category must be between 1 and 100 characters")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @DynamoDbSecondarySortKey(indexNames = "CategoryWithNameIndex")
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 300 characters")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

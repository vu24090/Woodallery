package com.showroom.repository;

import java.util.List;

import com.showroom.model.Product;

import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class ProductRepository {
    private final DynamoDbTable<Product> productTable;
    private final DynamoDbIndex<Product> nameIndex;

    public ProductRepository() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder().build();
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        
        productTable = enhancedClient.table("products", TableSchema.fromBean(Product.class));
        nameIndex = productTable.index("NameWithPriceIndex");
    }

    public void saveProduct(Product product) {
        productTable.putItem(product);
    }

    public Product getProductById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return productTable.getItem(GetItemEnhancedRequest.builder().key(key).build());
    }

    public List<Product> getAllProducts() {
        return productTable.scan().items().stream().toList();
    }

    public void deleteProduct(String id) {
        Key key = Key.builder().partitionValue(id).build();
        productTable.deleteItem(key);
    }

    //lọc theo tên & giá
    public List<Product> getProductsByName (String name) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(name).build());
        return nameIndex.query(queryConditional)
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

}

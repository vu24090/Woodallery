package com.showroom.service;

import com.showroom.model.Product;
import com.showroom.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;


class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void getAllProducts_shouldReturnProducts() {
        Product product1 = new Product();
        product1.setProductId("1");
        product1.setName("Chair");

        Product product2 = new Product();
        product2.setProductId("2");
        product2.setName("Table");

        when(productRepository.getAllProducts())
                .thenReturn(List.of(product1, product2));

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Chair", result.get(0).getName());
        assertEquals("Table", result.get(1).getName());

        verify(productRepository).getAllProducts();
    }

    @Test
    void getProductById_shouldReturnProduct_whenProductExists() {
        Product product = new Product();
        product.setProductId("123");
        product.setName("Chair");

        when(productRepository.getProductById("123"))
                .thenReturn(product);

        Product result = productService.getProductById("123");

        assertNotNull(result);
        assertEquals("123", result.getProductId());
        assertEquals("Chair", result.getName());

        verify(productRepository).getProductById("123");
    }

    @Test
    void getProductById_shouldReturnNull_whenProductDoesNotExist() {
        when(productRepository.getProductById("123"))
                .thenReturn(null);

        Product result = productService.getProductById("123");

        assertNull(result);

        verify(productRepository).getProductById("123");
    }

    @Test
    void createProduct_shouldGenerateIdSlugAndCreateAt() {
        Product product = new Product();
        product.setName("Modern Chair");

        Product result = productService.createProduct(product);

        assertNotNull(result.getProductId());
        assertFalse(result.getProductId().isBlank());

        assertEquals("modern-chair", result.getSlug());

        assertNotNull(result.getCreateAt());
        assertFalse(result.getCreateAt().isBlank());

        verify(productRepository).saveProduct(product);
    }

    @Test
    void updateProduct_shouldUpdateExistingProduct() {
        Product existingProduct = new Product();
        existingProduct.setProductId("123");
        existingProduct.setSlug("old-chair");
        existingProduct.setCreateAt("2026-01-01T00:00:00Z");

        Product updatedProduct = new Product();
        updatedProduct.setName("New Chair");

        when(productRepository.getProductById("123"))
                .thenReturn(existingProduct);

        Product result =
                productService.updateProduct("123", updatedProduct);

        assertNotNull(result);

        // ID, slug, createAt phải giữ nguyên
        assertEquals("123", result.getProductId());
        assertEquals("old-chair", result.getSlug());
        assertEquals(
                "2026-01-01T00:00:00Z",
                result.getCreateAt()
        );

        // Các field mới được update
        assertEquals("New Chair", result.getName());

        verify(productRepository).getProductById("123");
        verify(productRepository).saveProduct(updatedProduct);
    }

    @Test
    void updateProduct_shouldReturnNull_whenProductDoesNotExist() {
        when(productRepository.getProductById("123"))
                .thenReturn(null);

        Product product = new Product();
        product.setName("New Chair");

        Product result =
                productService.updateProduct("123", product);

        assertNull(result);

        verify(productRepository).getProductById("123");

        // Không được save nếu product không tồn tại
        verify(productRepository, never())
                .saveProduct(any(Product.class));
    }

    @Test
    void deleteProduct_shouldReturnTrue_whenProductExists() {
        Product product = new Product();
        product.setProductId("123");

        when(productRepository.getProductById("123"))
                .thenReturn(product);

        boolean result =
                productService.deleteProduct("123");

        assertTrue(result);

        verify(productRepository).getProductById("123");
        verify(productRepository).deleteProduct("123");
    }

    @Test
    void deleteProduct_shouldReturnFalse_whenProductDoesNotExist() {
        when(productRepository.getProductById("123"))
                .thenReturn(null);

        boolean result =
                productService.deleteProduct("123");

        assertFalse(result);

        verify(productRepository).getProductById("123");

        // Không được delete nếu product không tồn tại
        verify(productRepository, never())
                .deleteProduct(anyString());
    }

    @Test
    void getProductByCategoryKeyword_shouldFilterByKeyword() {
        Product chair = new Product();
        chair.setName("Modern Chair");

        Product table = new Product();
        table.setName("Modern Table");

        Product sofa = new Product();
        sofa.setName("Luxury Sofa");

        when(productRepository.getProductByCategoryName("furniture"))
                .thenReturn(List.of(chair, table, sofa));

        List<Product> result =
                productService.getProductByCategoryKeyword(
                        "furniture",
                        "chair"
                );

        assertEquals(1, result.size());
        assertEquals("Modern Chair", result.get(0).getName());

        verify(productRepository)
                .getProductByCategoryName("furniture");
    }

    @Test
    void getProductByCategoryKeyword_shouldBeCaseInsensitive() {
        Product chair = new Product();
        chair.setName("Modern Chair");

        when(productRepository.getProductByCategoryName("furniture"))
                .thenReturn(List.of(chair));

        List<Product> result =
                productService.getProductByCategoryKeyword(
                        "furniture",
                        "CHAIR"
                );

        assertEquals(1, result.size());
        assertEquals("Modern Chair", result.get(0).getName());
    }

    @Test
    void getProductByCategoryKeyword_shouldReturnAllProducts_whenKeywordIsBlank() {
        Product chair = new Product();
        chair.setName("Modern Chair");

        Product table = new Product();
        table.setName("Modern Table");

        when(productRepository.getProductByCategoryName("furniture"))
                .thenReturn(List.of(chair, table));

        List<Product> result =
                productService.getProductByCategoryKeyword(
                        "furniture",
                        ""
                );

        assertEquals(2, result.size());
        assertEquals("Modern Chair", result.get(0).getName());
        assertEquals("Modern Table", result.get(1).getName());

        verify(productRepository)
                .getProductByCategoryName("furniture");
    }

    @Test
    void getProductByCategoryKeyword_shouldReturnAllProducts_whenKeywordIsNull() {
        Product chair = new Product();
        chair.setName("Modern Chair");

        Product table = new Product();
        table.setName("Modern Table");

        when(productRepository.getProductByCategoryName("furniture"))
                .thenReturn(List.of(chair, table));

        List<Product> result =
                productService.getProductByCategoryKeyword(
                        "furniture",
                        null
                );

        assertEquals(2, result.size());

        verify(productRepository)
                .getProductByCategoryName("furniture");
    }

    @Test
    void getProductByCategoryKeyword_shouldIgnoreProductWithNullName() {
        Product productWithoutName = new Product();

        Product chair = new Product();
        chair.setName("Modern Chair");

        when(productRepository.getProductByCategoryName("furniture"))
                .thenReturn(List.of(productWithoutName, chair));

        List<Product> result =
                productService.getProductByCategoryKeyword(
                        "furniture",
                        "chair"
                );

        assertEquals(1, result.size());
        assertEquals("Modern Chair", result.get(0).getName());
    }
}
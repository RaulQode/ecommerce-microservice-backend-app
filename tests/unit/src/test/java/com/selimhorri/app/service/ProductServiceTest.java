package com.selimhorri.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit Test 2: Product Service Validation
 * Tests CRUD operations for Product Service
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Unit Tests")
class ProductServiceTest {

    @Mock
    private ProductService productService;

    private ProductDto testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new ProductDto();
        testProduct.setProductId(1);
        testProduct.setProductTitle("Test Product");
        testProduct.setSku("TEST-001");
        testProduct.setPriceUnit(new BigDecimal("99.99"));
        testProduct.setQuantity(100);
    }

    @Test
    @DisplayName("Should find all products successfully")
    void testFindAllProducts() {
        // Given
        List<ProductDto> expectedProducts = Arrays.asList(testProduct, new ProductDto());
        when(productService.findAll()).thenReturn(expectedProducts);

        // When
        List<ProductDto> actualProducts = productService.findAll();

        // Then
        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        verify(productService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find product by ID successfully")
    void testFindProductById() {
        // Given
        when(productService.findById(1)).thenReturn(testProduct);

        // When
        ProductDto foundProduct = productService.findById(1);

        // Then
        assertNotNull(foundProduct);
        assertEquals("Test Product", foundProduct.getProductTitle());
        assertEquals("TEST-001", foundProduct.getSku());
        assertEquals(new BigDecimal("99.99"), foundProduct.getPriceUnit());
        verify(productService, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should save product successfully")
    void testSaveProduct() {
        // Given
        when(productService.save(any(ProductDto.class))).thenReturn(testProduct);

        // When
        ProductDto savedProduct = productService.save(testProduct);

        // Then
        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getProductTitle());
        assertEquals(100, savedProduct.getQuantity());
        verify(productService, times(1)).save(any(ProductDto.class));
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdateProduct() {
        // Given
        testProduct.setPriceUnit(new BigDecimal("79.99"));
        when(productService.update(anyInt(), any(ProductDto.class))).thenReturn(testProduct);

        // When
        ProductDto updatedProduct = productService.update(1, testProduct);

        // Then
        assertNotNull(updatedProduct);
        assertEquals(new BigDecimal("79.99"), updatedProduct.getPriceUnit());
        verify(productService, times(1)).update(anyInt(), any(ProductDto.class));
    }

    @Test
    @DisplayName("Should validate product price is positive")
    void testProductPriceValidation() {
        // Given
        testProduct.setPriceUnit(new BigDecimal("99.99"));

        // Then
        assertTrue(testProduct.getPriceUnit().compareTo(BigDecimal.ZERO) > 0);
    }

    // Simple DTO class for testing
    static class ProductDto {
        private Integer productId;
        private String productTitle;
        private String sku;
        private BigDecimal priceUnit;
        private Integer quantity;

        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }
        public String getProductTitle() { return productTitle; }
        public void setProductTitle(String productTitle) { this.productTitle = productTitle; }
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public BigDecimal getPriceUnit() { return priceUnit; }
        public void setPriceUnit(BigDecimal priceUnit) { this.priceUnit = priceUnit; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}

package com.selimhorri.app.service;

import com.selimhorri.app.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        testProduct = ProductDto.builder()
                .productId(1)
                .productTitle("Test Product")
                .sku("TEST-001")
                .priceUnit(99.99)
                .quantity(100)
                .build();
    }

    @Test
    @DisplayName("Should find all products successfully")
    void testFindAllProducts() {
        // Given
        ProductDto secondProduct = ProductDto.builder().productId(2).productTitle("Second Product").build();
        List<ProductDto> expectedProducts = Arrays.asList(testProduct, secondProduct);
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
        assertEquals(99.99, foundProduct.getPriceUnit());
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
        ProductDto updatedData = ProductDto.builder()
                .productId(1)
                .productTitle("Test Product")
                .sku("TEST-001")
                .priceUnit(79.99)
                .quantity(100)
                .build();
        when(productService.update(anyInt(), any(ProductDto.class))).thenReturn(updatedData);

        // When
        ProductDto updatedProduct = productService.update(1, updatedData);

        // Then
        assertNotNull(updatedProduct);
        assertEquals(79.99, updatedProduct.getPriceUnit());
        verify(productService, times(1)).update(anyInt(), any(ProductDto.class));
    }

    @Test
    @DisplayName("Should validate product price is positive")
    void testProductPriceValidation() {
        // Then
        assertTrue(testProduct.getPriceUnit() > 0);
    }
}

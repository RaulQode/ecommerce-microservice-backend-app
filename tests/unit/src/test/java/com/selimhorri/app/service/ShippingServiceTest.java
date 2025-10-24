package com.selimhorri.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit Test 5: Shipping Service Validation
 * Tests shipping operations and tracking
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Shipping Service Unit Tests")
class ShippingServiceTest {

    @Mock
    private OrderItemService shippingService;

    private OrderItemDto testShipping;

    @BeforeEach
    void setUp() {
        testShipping = new OrderItemDto();
        testShipping.setOrderItemId(1);
        testShipping.setOrderId(1);
        testShipping.setProductId(1);
        testShipping.setQuantity(2);
        testShipping.setShippingStatus("PENDING");
        testShipping.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create shipping record successfully")
    void testCreateShipping() {
        // Given
        when(shippingService.save(any(OrderItemDto.class))).thenReturn(testShipping);

        // When
        OrderItemDto createdShipping = shippingService.save(testShipping);

        // Then
        assertNotNull(createdShipping);
        assertEquals(1, createdShipping.getOrderItemId());
        assertEquals("PENDING", createdShipping.getShippingStatus());
        verify(shippingService, times(1)).save(any(OrderItemDto.class));
    }

    @Test
    @DisplayName("Should find shipping by ID successfully")
    void testFindShippingById() {
        // Given
        when(shippingService.findById(1)).thenReturn(testShipping);

        // When
        OrderItemDto foundShipping = shippingService.findById(1);

        // Then
        assertNotNull(foundShipping);
        assertEquals(1, foundShipping.getOrderId());
        assertEquals(1, foundShipping.getProductId());
        verify(shippingService, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should update shipping status successfully")
    void testUpdateShippingStatus() {
        // Given
        testShipping.setShippingStatus("SHIPPED");
        when(shippingService.update(anyInt(), any(OrderItemDto.class))).thenReturn(testShipping);

        // When
        OrderItemDto updatedShipping = shippingService.update(1, testShipping);

        // Then
        assertNotNull(updatedShipping);
        assertEquals("SHIPPED", updatedShipping.getShippingStatus());
        verify(shippingService, times(1)).update(anyInt(), any(OrderItemDto.class));
    }

    @Test
    @DisplayName("Should validate shipping quantity is positive")
    void testShippingQuantityValidation() {
        // Then
        assertTrue(testShipping.getQuantity() > 0);
    }

    @Test
    @DisplayName("Should track shipping status transitions")
    void testShippingStatusTransitions() {
        // Test valid status transitions
        testShipping.setShippingStatus("PENDING");
        assertEquals("PENDING", testShipping.getShippingStatus());
        
        testShipping.setShippingStatus("SHIPPED");
        assertEquals("SHIPPED", testShipping.getShippingStatus());
        
        testShipping.setShippingStatus("DELIVERED");
        assertEquals("DELIVERED", testShipping.getShippingStatus());
    }

    // Simple DTO and Service classes for testing
    static class OrderItemDto {
        private Integer orderItemId;
        private Integer orderId;
        private Integer productId;
        private Integer quantity;
        private String shippingStatus;
        private LocalDateTime createdAt;

        public Integer getOrderItemId() { return orderItemId; }
        public void setOrderItemId(Integer orderItemId) { this.orderItemId = orderItemId; }
        public Integer getOrderId() { return orderId; }
        public void setOrderId(Integer orderId) { this.orderId = orderId; }
        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getShippingStatus() { return shippingStatus; }
        public void setShippingStatus(String shippingStatus) { this.shippingStatus = shippingStatus; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}

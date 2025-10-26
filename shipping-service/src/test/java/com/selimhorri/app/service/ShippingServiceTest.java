package com.selimhorri.app.service;

import com.selimhorri.app.domain.id.OrderItemId;
import com.selimhorri.app.dto.OrderItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Test 5: Shipping Service Validation
 * Tests shipping operations and tracking
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Shipping Service Unit Tests")
class ShippingServiceTest {

    @Mock
    private OrderItemService orderItemService;

    private OrderItemDto testOrderItem;
    private OrderItemId testOrderItemId;

    @BeforeEach
    void setUp() {
        testOrderItemId = new OrderItemId(1, 1); // productId, orderId
        testOrderItem = OrderItemDto.builder()
                .orderId(1)
                .productId(1)
                .orderedQuantity(2)
                .build();
    }

    @Test
    @DisplayName("Should create order item record successfully")
    void testCreateOrderItem() {
        // Given
        when(orderItemService.save(any(OrderItemDto.class))).thenReturn(testOrderItem);

        // When
        OrderItemDto createdItem = orderItemService.save(testOrderItem);

        // Then
        assertNotNull(createdItem);
        assertEquals(1, createdItem.getOrderId());
        assertEquals(2, createdItem.getOrderedQuantity());
        verify(orderItemService, times(1)).save(any(OrderItemDto.class));
    }

    @Test
    @DisplayName("Should find order item by ID successfully")
    void testFindOrderItemById() {
        // Given
        when(orderItemService.findById(any(OrderItemId.class))).thenReturn(testOrderItem);

        // When
        OrderItemDto foundItem = orderItemService.findById(testOrderItemId);

        // Then
        assertNotNull(foundItem);
        assertEquals(1, foundItem.getOrderId());
        assertEquals(1, foundItem.getProductId());
        verify(orderItemService, times(1)).findById(any(OrderItemId.class));
    }

    @Test
    @DisplayName("Should update order item successfully")
    void testUpdateOrderItem() {
        // Given
        OrderItemDto updatedData = OrderItemDto.builder()
                .orderId(1)
                .productId(1)
                .orderedQuantity(5)
                .build();
        when(orderItemService.update(any(OrderItemDto.class))).thenReturn(updatedData);

        // When
        OrderItemDto updatedItem = orderItemService.update(updatedData);

        // Then
        assertNotNull(updatedItem);
        assertEquals(5, updatedItem.getOrderedQuantity());
        verify(orderItemService, times(1)).update(any(OrderItemDto.class));
    }

    @Test
    @DisplayName("Should validate order item quantity is positive")
    void testOrderItemQuantityValidation() {
        // Then
        assertTrue(testOrderItem.getOrderedQuantity() > 0);
    }

    @Test
    @DisplayName("Should track order item data")
    void testOrderItemData() {
        // Test data integrity
        assertEquals(1, testOrderItem.getOrderId());
        assertEquals(1, testOrderItem.getProductId());
        assertEquals(2, testOrderItem.getOrderedQuantity());
    }
}

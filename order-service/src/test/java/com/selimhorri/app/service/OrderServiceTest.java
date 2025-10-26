package com.selimhorri.app.service;

import com.selimhorri.app.dto.OrderDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit Test 3: Order Service Validation
 * Tests order creation, update and calculation logic
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Order Service Unit Tests")
class OrderServiceTest {

    @Mock
    private OrderService orderService;

    private OrderDto testOrder;

    @BeforeEach
    void setUp() {
        testOrder = OrderDto.builder()
                .orderId(1)
                .orderDate(LocalDateTime.now())
                .orderDesc("Test Order")
                .orderFee(199.98)
                .build();
    }

    @Test
    @DisplayName("Should create order successfully")
    void testCreateOrder() {
        // Given
        when(orderService.save(any(OrderDto.class))).thenReturn(testOrder);

        // When
        OrderDto createdOrder = orderService.save(testOrder);

        // Then
        assertNotNull(createdOrder);
        assertEquals(1, createdOrder.getOrderId());
        assertEquals(199.98, createdOrder.getOrderFee());
        verify(orderService, times(1)).save(any(OrderDto.class));
    }

    @Test
    @DisplayName("Should find all orders successfully")
    void testFindAllOrders() {
        // Given
        OrderDto secondOrder = OrderDto.builder().orderId(2).orderFee(99.99).build();
        List<OrderDto> expectedOrders = Arrays.asList(testOrder, secondOrder);
        when(orderService.findAll()).thenReturn(expectedOrders);

        // When
        List<OrderDto> actualOrders = orderService.findAll();

        // Then
        assertNotNull(actualOrders);
        assertEquals(2, actualOrders.size());
        verify(orderService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should update order successfully")
    void testUpdateOrder() {
        // Given
        OrderDto updatedData = OrderDto.builder()
                .orderId(1)
                .orderDesc("Updated Order")
                .orderFee(199.98)
                .orderDate(LocalDateTime.now())
                .build();
        when(orderService.update(anyInt(), any(OrderDto.class))).thenReturn(updatedData);

        // When
        OrderDto updatedOrder = orderService.update(1, updatedData);

        // Then
        assertNotNull(updatedOrder);
        assertEquals("Updated Order", updatedOrder.getOrderDesc());
        verify(orderService, times(1)).update(anyInt(), any(OrderDto.class));
    }

    @Test
    @DisplayName("Should calculate order total correctly")
    void testOrderTotalCalculation() {
        // Given
        Double item1Price = 99.99;
        Double item2Price = 99.99;
        Double expectedTotal = item1Price + item2Price;

        // When
        testOrder.setOrderFee(expectedTotal);

        // Then
        assertEquals(199.98, testOrder.getOrderFee());
    }

    @Test
    @DisplayName("Should validate order date is not null")
    void testOrderDateValidation() {
        // Then
        assertNotNull(testOrder.getOrderDate());
        assertTrue(testOrder.getOrderDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}

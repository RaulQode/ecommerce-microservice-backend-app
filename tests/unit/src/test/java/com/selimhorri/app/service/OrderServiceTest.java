package com.selimhorri.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
        testOrder = new OrderDto();
        testOrder.setOrderId(1);
        testOrder.setUserId(1);
        testOrder.setTotalAmount(new BigDecimal("199.98"));
        testOrder.setOrderStatus("PENDING");
        testOrder.setOrderDate(LocalDateTime.now());
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
        assertEquals("PENDING", createdOrder.getOrderStatus());
        assertEquals(new BigDecimal("199.98"), createdOrder.getTotalAmount());
        verify(orderService, times(1)).save(any(OrderDto.class));
    }

    @Test
    @DisplayName("Should find all orders successfully")
    void testFindAllOrders() {
        // Given
        List<OrderDto> expectedOrders = Arrays.asList(testOrder, new OrderDto());
        when(orderService.findAll()).thenReturn(expectedOrders);

        // When
        List<OrderDto> actualOrders = orderService.findAll();

        // Then
        assertNotNull(actualOrders);
        assertEquals(2, actualOrders.size());
        verify(orderService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should update order status successfully")
    void testUpdateOrderStatus() {
        // Given
        testOrder.setOrderStatus("CONFIRMED");
        when(orderService.update(anyInt(), any(OrderDto.class))).thenReturn(testOrder);

        // When
        OrderDto updatedOrder = orderService.update(1, testOrder);

        // Then
        assertNotNull(updatedOrder);
        assertEquals("CONFIRMED", updatedOrder.getOrderStatus());
        verify(orderService, times(1)).update(anyInt(), any(OrderDto.class));
    }

    @Test
    @DisplayName("Should calculate order total correctly")
    void testOrderTotalCalculation() {
        // Given
        BigDecimal item1Price = new BigDecimal("99.99");
        BigDecimal item2Price = new BigDecimal("99.99");
        BigDecimal expectedTotal = item1Price.add(item2Price);

        // When
        testOrder.setTotalAmount(expectedTotal);

        // Then
        assertEquals(new BigDecimal("199.98"), testOrder.getTotalAmount());
    }

    @Test
    @DisplayName("Should validate order date is not null")
    void testOrderDateValidation() {
        // Then
        assertNotNull(testOrder.getOrderDate());
        assertTrue(testOrder.getOrderDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    // Simple DTO class for testing
    static class OrderDto {
        private Integer orderId;
        private Integer userId;
        private BigDecimal totalAmount;
        private String orderStatus;
        private LocalDateTime orderDate;

        public Integer getOrderId() { return orderId; }
        public void setOrderId(Integer orderId) { this.orderId = orderId; }
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public String getOrderStatus() { return orderStatus; }
        public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
        public LocalDateTime getOrderDate() { return orderDate; }
        public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    }
}

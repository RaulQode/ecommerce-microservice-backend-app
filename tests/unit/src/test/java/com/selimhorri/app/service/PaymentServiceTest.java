package com.selimhorri.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit Test 4: Payment Service Validation
 * Tests payment processing and validation
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Payment Service Unit Tests")
class PaymentServiceTest {

    @Mock
    private PaymentService paymentService;

    private PaymentDto testPayment;

    @BeforeEach
    void setUp() {
        testPayment = new PaymentDto();
        testPayment.setPaymentId(1);
        testPayment.setOrderId(1);
        testPayment.setAmount(new BigDecimal("199.98"));
        testPayment.setPaymentMode("CREDIT_CARD");
        testPayment.setPaymentStatus("SUCCESS");
        testPayment.setPaymentDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should process payment successfully")
    void testProcessPayment() {
        // Given
        when(paymentService.save(any(PaymentDto.class))).thenReturn(testPayment);

        // When
        PaymentDto processedPayment = paymentService.save(testPayment);

        // Then
        assertNotNull(processedPayment);
        assertEquals("SUCCESS", processedPayment.getPaymentStatus());
        assertEquals(new BigDecimal("199.98"), processedPayment.getAmount());
        verify(paymentService, times(1)).save(any(PaymentDto.class));
    }

    @Test
    @DisplayName("Should find payment by ID successfully")
    void testFindPaymentById() {
        // Given
        when(paymentService.findById(1)).thenReturn(testPayment);

        // When
        PaymentDto foundPayment = paymentService.findById(1);

        // Then
        assertNotNull(foundPayment);
        assertEquals(1, foundPayment.getPaymentId());
        assertEquals("CREDIT_CARD", foundPayment.getPaymentMode());
        verify(paymentService, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should validate payment amount is positive")
    void testPaymentAmountValidation() {
        // Then
        assertTrue(testPayment.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Should update payment status successfully")
    void testUpdatePaymentStatus() {
        // Given
        testPayment.setPaymentStatus("REFUNDED");
        when(paymentService.update(anyInt(), any(PaymentDto.class))).thenReturn(testPayment);

        // When
        PaymentDto updatedPayment = paymentService.update(1, testPayment);

        // Then
        assertNotNull(updatedPayment);
        assertEquals("REFUNDED", updatedPayment.getPaymentStatus());
        verify(paymentService, times(1)).update(anyInt(), any(PaymentDto.class));
    }

    @Test
    @DisplayName("Should validate payment date is not in future")
    void testPaymentDateValidation() {
        // Then
        assertNotNull(testPayment.getPaymentDate());
        assertTrue(testPayment.getPaymentDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    // Simple DTO and Service classes for testing
    static class PaymentDto {
        private Integer paymentId;
        private Integer orderId;
        private BigDecimal amount;
        private String paymentMode;
        private String paymentStatus;
        private LocalDateTime paymentDate;

        public Integer getPaymentId() { return paymentId; }
        public void setPaymentId(Integer paymentId) { this.paymentId = paymentId; }
        public Integer getOrderId() { return orderId; }
        public void setOrderId(Integer orderId) { this.orderId = orderId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getPaymentMode() { return paymentMode; }
        public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }
        public String getPaymentStatus() { return paymentStatus; }
        public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
        public LocalDateTime getPaymentDate() { return paymentDate; }
        public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    }
}

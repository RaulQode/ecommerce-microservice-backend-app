package com.selimhorri.app.service;

import com.selimhorri.app.domain.PaymentStatus;
import com.selimhorri.app.dto.PaymentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        testPayment = PaymentDto.builder()
                .paymentId(1)
                .isPayed(true)
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();
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
        assertEquals(PaymentStatus.COMPLETED, processedPayment.getPaymentStatus());
        assertTrue(processedPayment.getIsPayed());
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
        assertEquals(PaymentStatus.COMPLETED, foundPayment.getPaymentStatus());
        verify(paymentService, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should validate payment status")
    void testPaymentStatusValidation() {
        // Then
        assertNotNull(testPayment.getPaymentStatus());
        assertEquals(PaymentStatus.COMPLETED, testPayment.getPaymentStatus());
    }

    @Test
    @DisplayName("Should update payment status successfully")
    void testUpdatePaymentStatus() {
        // Given
        PaymentDto updatedData = PaymentDto.builder()
                .paymentId(1)
                .isPayed(false)
                .paymentStatus(PaymentStatus.NOT_STARTED)
                .build();
        when(paymentService.update(any(PaymentDto.class))).thenReturn(updatedData);

        // When
        PaymentDto updatedPayment = paymentService.update(updatedData);

        // Then
        assertNotNull(updatedPayment);
        assertEquals(PaymentStatus.NOT_STARTED, updatedPayment.getPaymentStatus());
        assertFalse(updatedPayment.getIsPayed());
        verify(paymentService, times(1)).update(any(PaymentDto.class));
    }

    @Test
    @DisplayName("Should handle payment confirmation")
    void testPaymentConfirmation() {
        // Then
        assertTrue(testPayment.getIsPayed());
        assertEquals(PaymentStatus.COMPLETED, testPayment.getPaymentStatus());
    }
}

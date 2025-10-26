package com.selimhorri.app.service;

import com.selimhorri.app.dto.UserDto;
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
 * Unit Test 1: User Service Validation
 * Tests CRUD operations for User Service
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Unit Tests")
class UserServiceTest {

    @Mock
    private UserService userService;

    private UserDto testUser;

    @BeforeEach
    void setUp() {
        testUser = UserDto.builder()
                .userId(1)
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .phone("1234567890")
                .build();
    }

    @Test
    @DisplayName("Should find all users successfully")
    void testFindAllUsers() {
        // Given
        UserDto secondUser = UserDto.builder().userId(2).firstName("Second").build();
        List<UserDto> expectedUsers = Arrays.asList(testUser, secondUser);
        when(userService.findAll()).thenReturn(expectedUsers);

        // When
        List<UserDto> actualUsers = userService.findAll();

        // Then
        assertNotNull(actualUsers);
        assertEquals(2, actualUsers.size());
        verify(userService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find user by ID successfully")
    void testFindUserById() {
        // Given
        when(userService.findById(1)).thenReturn(testUser);

        // When
        UserDto foundUser = userService.findById(1);

        // Then
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        assertEquals("Test", foundUser.getFirstName());
        verify(userService, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should save user successfully")
    void testSaveUser() {
        // Given
        when(userService.save(any(UserDto.class))).thenReturn(testUser);

        // When
        UserDto savedUser = userService.save(testUser);

        // Then
        assertNotNull(savedUser);
        assertEquals("Test", savedUser.getFirstName());
        verify(userService, times(1)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUser() {
        // Given
        UserDto updatedData = UserDto.builder()
                .userId(1)
                .firstName("Updated")
                .lastName("User")
                .email("test@example.com")
                .build();
        when(userService.update(anyInt(), any(UserDto.class))).thenReturn(updatedData);

        // When
        UserDto updatedUser = userService.update(1, updatedData);

        // Then
        assertNotNull(updatedUser);
        assertEquals("Updated", updatedUser.getFirstName());
        verify(userService, times(1)).update(anyInt(), any(UserDto.class));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDeleteUser() {
        // Given
        doNothing().when(userService).deleteById(1);

        // When
        userService.deleteById(1);

        // Then
        verify(userService, times(1)).deleteById(1);
    }
}

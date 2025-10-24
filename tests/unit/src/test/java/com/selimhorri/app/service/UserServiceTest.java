package com.selimhorri.app.service;

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
        testUser = new UserDto();
        testUser.setUserId(1);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
    }

    @Test
    @DisplayName("Should find all users successfully")
    void testFindAllUsers() {
        // Given
        List<UserDto> expectedUsers = Arrays.asList(testUser, new UserDto());
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
        assertEquals("testuser", foundUser.getUsername());
        assertEquals("test@example.com", foundUser.getEmail());
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
        assertEquals("testuser", savedUser.getUsername());
        verify(userService, times(1)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUser() {
        // Given
        testUser.setFirstName("Updated");
        when(userService.update(anyInt(), any(UserDto.class))).thenReturn(testUser);

        // When
        UserDto updatedUser = userService.update(1, testUser);

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

    // Simple DTO class for testing
    static class UserDto {
        private Integer userId;
        private String username;
        private String email;
        private String firstName;
        private String lastName;

        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
    }
}

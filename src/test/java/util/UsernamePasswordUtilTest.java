package util;

import com.epam.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.epam.util.UsernamePasswordUtil;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsernamePasswordUtilTest {

    @Mock
    private UserRepository userRepository;

    private UsernamePasswordUtil util;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        util = new UsernamePasswordUtil(userRepository);
    }

    @Test
    void generateUsername_NoConflict() {
        when(userRepository.existsByUsername("john.doe")).thenReturn(false);

        String username = util.generateUsername("John", "Doe");

        assertEquals("john.doe", username);
        verify(userRepository).existsByUsername("john.doe");
    }

    @Test
    void generateUsername_WithConflict() {
        when(userRepository.existsByUsername("john.doe")).thenReturn(true);
        when(userRepository.existsByUsername("john.doe1")).thenReturn(true);
        when(userRepository.existsByUsername("john.doe2")).thenReturn(false);

        String username = util.generateUsername("John", "Doe");

        assertEquals("john.doe2", username);
        verify(userRepository, times(3)).existsByUsername(anyString());
    }

    @Test
    void generatePassword_HasCorrectLengthAndCharset() {
        String password = util.generatePassword();

        assertNotNull(password);
        assertEquals(10, password.length());

        String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (char c : password.toCharArray()) {
            assertTrue(validChars.indexOf(c) >= 0, "Invalid character: " + c);
        }
    }

    @Test
    void hashPassword_ReturnsPlainText() {
        String password = "mypassword";
        String hashed = util.hashPassword(password);

        assertEquals(password, hashed); // since this is a placeholder
    }

    @Test
    void checkPassword_Matching() {
        assertTrue(util.checkPassword("password", "password"));
    }

    @Test
    void checkPassword_NotMatching() {
        assertFalse(util.checkPassword("password", "different"));
    }
}


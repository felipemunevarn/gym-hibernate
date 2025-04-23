package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import com.epam.entity.User;
import com.epam.repository.UserRepository;
import com.epam.service.UserService;
import com.epam.util.UsernamePasswordUtil;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UsernamePasswordUtil usernamePasswordUtil;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .password("hashedPassword")
                .isActive(true)
                .build();
    }

    @Test
    void createUser_Success() {
        when(usernamePasswordUtil.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(usernamePasswordUtil.generatePassword()).thenReturn("tempPassword123");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        userService.createUser(testUser);

        verify(usernamePasswordUtil).generateUsername("John", "Doe");
        verify(usernamePasswordUtil).generatePassword();
        verify(userRepository).save(argThat(user ->
                user.getUsername().equals("john.doe") &&
                        user.getPassword().equals("tempPassword123")
        ));
    }

    @Test
    void findByUsername_UserExists() {
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findByUsername("john.doe");

        assertTrue(result.isPresent());
        assertEquals("john.doe", result.get().getUsername());
    }

    @Test
    void authenticate_Success() {
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(usernamePasswordUtil.checkPassword("password123", "hashedPassword")).thenReturn(true);

        boolean result = userService.authenticate("john.doe", "password123");

        assertTrue(result);
    }

    @Test
    void authenticate_WrongPassword() {
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));
        when(usernamePasswordUtil.checkPassword("wrongpass", "hashedPassword")).thenReturn(false);

        boolean result = userService.authenticate("john.doe", "wrongpass");

        assertFalse(result);
    }

    @Test
    void authenticate_UserInactive() {
        User inactiveUser = testUser.toBuilder()
                    .isActive(false)
                    .build();
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(inactiveUser));

        boolean result = userService.authenticate("john.doe", "password123");

        assertFalse(result);
    }

    @Test
    void authenticate_UserNotFound() {
        when(userRepository.findByUsername("non.existing")).thenReturn(Optional.empty());

        boolean result = userService.authenticate("non.existing", "password123");

        assertFalse(result);
    }

    @Test
    void createUser_ExceptionHandling() {
        when(usernamePasswordUtil.generateUsername(anyString(), anyString())).thenReturn("john.doe");
        when(usernamePasswordUtil.generatePassword()).thenReturn("tempPassword123");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("DB Error"));

        assertDoesNotThrow(() -> userService.createUser(testUser));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_Success() {
        when(userRepository.findByUsername("john.doe"))
                .thenReturn(Optional.of(testUser));
        when(usernamePasswordUtil.checkPassword("oldPass", "hashedPassword"))
                .thenReturn(true);
        when(usernamePasswordUtil.hashPassword("newPass"))
                .thenReturn("hashedNewPass");

        userService.changePassword("john.doe", "oldPass", "newPass");

        verify(userRepository).save(argThat(user -> user.getPassword().equals("hashedNewPass")));
    }

    @Test
    void changePassword_InvalidOldPassword() {
        when(userRepository.findByUsername("john.doe"))
                .thenReturn(Optional.of(testUser));
        when(usernamePasswordUtil.checkPassword("wrongOldPass", "hashedPassword"))
                .thenReturn(false);

        assertThrows(SecurityException.class, () ->
                userService.changePassword("john.doe", "wrongOldPass", "newPass"));
    }

    @Test
    void changePassword_NewPasswordBlank() {
        when(userRepository.findByUsername("john.doe"))
                .thenReturn(Optional.of(testUser));
        when(usernamePasswordUtil.checkPassword("oldPass", "hashedPassword"))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                userService.changePassword("john.doe", "oldPass", " "));
    }

    @Test
    void setActiveStatus_Success() {
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(testUser));

        userService.setActiveStatus("john.doe", false);

        verify(userRepository).save(argThat(user -> !user.isActive()));
    }

    @Test
    void setActiveStatus_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(NoResultException.class, () -> userService.setActiveStatus("unknown", true));
    }

}
